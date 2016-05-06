(ns leiningen.new.cubane
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files
                                             sanitize sanitize-ns project-name]]
            [leiningen.core.main :as main]
            [clojure.string :as s]
            [clojure.java.io :as io]
            [clj-jgit.porcelain :refer :all]))

(def render (renderer "cubane"))

(defn- wrap-indent [wrap n list]
  (fn []
    (->> list
         (map #(str "\n" (apply str (repeat n " ")) (wrap %)))
         (s/join ""))))

(defn- dep-list [n list]
  (wrap-indent #(str "[" % "]") n list))

(def valid-options
  ["http-kit"
   "orient-db"])

(doseq [opt valid-options]
  (eval
    `(defn ~(symbol (str opt "?")) [opts#]
       (some #{~(str "--" opt)} opts#))))

(defn- app-clj-requires [sanitized-ns opts]
  (concat []
          (when (orient-db? opts)
            ["cubanostack.components.orientdb :as orientdb"])))

(defn- server-clj-requires [opts]
  (if (http-kit? opts)
    ["org.httpkit.server :refer [run-server]"]
    ["ring.adapter.jetty :refer [run-jetty]"]))

(defn- project-clj-profiles-env [opts]
  (concat []
          (when (orient-db? opts)
            [":orient-db-store \"memory:dev\""])))

(defn- project-clj-deps [opts]
  (concat []
          (when (http-kit? opts)
            ["http-kit \"2.1.19\""])
          (when (orient-db? opts)
            ["com.orientechnologies/orientdb-server \"2.1.13\""
             "com.orientechnologies/orientdb-core   \"2.1.13\""])))

(defn- load-props [file-name]
  (with-open [^java.io.Reader reader (clojure.java.io/reader file-name)]
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) v])))))

(defn- cubane-version []
  (let [resource  (io/resource "META-INF/maven/cubane/lein-template/pom.properties")
        props     (load-props resource)
        version   (:version props)
        revision  (:revision props)
        snapshot? (re-find #"SNAPSHOT" version)]
    (str version " (" (s/join (take 8 revision)) ")")))

(defn- template-data [name opts]
  (let [sanitized-ns (sanitize name)]
    {:full-name                name
     :name                     (project-name name)
     :cubane-version           (cubane-version)
     :project-ns               sanitized-ns
     :sanitized                (name-to-path name)
     :app-clj-requires         (dep-list 4 (app-clj-requires sanitized-ns opts))
     :server-clj-requires      (dep-list 4 (server-clj-requires opts))

     :project-clj-deps         (dep-list 23 (project-clj-deps opts))
     :project-clj-profiles-env (wrap-indent identity 40 (project-clj-profiles-env opts))

     :server-command           (if (http-kit? opts) "run-server" "run-jetty")

     ; features
     :orient-db?               (fn [block] (when (orient-db? opts) (str block)))
     }))

(defn- files-to-render [opts]
  (concat
    ["project.clj"
     "resources/public/index.html"
     "resources/log4j.properties"

     "src/bo/cubane/app.clj"
     "src/bo/cubane/components/chat_broadcaster.clj"
     "src/bo/cubane/components/fixtures.clj"
     "src/bo/cubane/components/home.clj"
     "src/bo/cubane/components/sente_broadcaster.clj"
     "src/bo/cubane/main.clj"
     "src/bo/cubane/tweet/dal.clj"
     "src/bo/cubane/tweet/service.clj"

     "src/common/cubane/tweet/model.cljc"

     "src/fo/cubane/core.cljs"
     "src/fo/cubane/chat/module.cljs"
     "src/fo/cubane/chat/ui.cljs"
     "src/fo/cubane/figwheel.cljs"
     "src/fo/cubane/home/module.cljs"
     "src/fo/cubane/home/ui.cljs"
     "src/fo/cubane/nav/ui.cljs"
     "src/fo/cubane/system.cljs"
     "src/fo/cubane/templates/ui.cljs"
     "src/fo/cubane/test_cmp/module.cljs"
     "src/fo/cubane/test_cmp/ui.cljs"
     "src/fo/cubane/tweet/module.cljs"
     "src/fo/cubane/tweet/rest.cljs"
     "src/fo/cubane/tweet/ui.cljs"

     "dev/user.clj"
     "LICENSE"
     "README.md"
     ".gitignore"
     "system.properties"

     "test/bo/cubane/integrated/crud.clj"
     "test/bo/cubane/integrated/tweet_test.clj"
     "test/bo/cubane/test_core.clj"

     "test/fo/cubane/test_runner.cljs"

     "src/scss/app.scss"

     "resources/public/css/app.css"
     "resources/public/robots.txt"]))

(defn- format-files-args [name opts]
  (let [data        (template-data name opts)
        render-file (fn [file]
                      [(s/replace file "cubane" "{{sanitized}}")
                       (render file data)])]
    (cons data
          (map render-file
               (files-to-render opts)))))


(defn cubane [name & opts]
  (let [valid-opts (map (partial str "--")
                        valid-options)]
    (doseq [opt opts]
      (when-not (some #{opt} valid-opts)
        (apply main/abort "Unrecognized option:" opt ". Should be one of" valid-opts))))

  (main/info "Generating fresh cubane project.")
  (main/info "README.md contains instructions to get you started.")

  (apply ->files
         (format-files-args name
                            opts))

  (git-init name)
  (let [repo (load-repo name)]
    (git-add repo ".")
    (git-commit repo
                (str "lein new cubane " name
                     (s/join " " opts)))))
