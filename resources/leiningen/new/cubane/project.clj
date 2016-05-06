(defproject {{full-name}} "0.1.0-SNAPSHOT"
  :description        "FIXME: write description"

  :license            {:name "Eclipse Public License"
                       :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies       [[org.clojure/clojure "1.8.0"]
                       [org.clojure/clojurescript "1.8.40"]

                       [org.clojure/core.async "0.2.371"]

                       [cubane/stack "0.1.0-SNAPSHOT"]

{{{project-clj-deps}}}]

  :plugins            [[lein-ring "0.9.6"]
                       [lein-cljsbuild "1.1.2"]
                       [lein-sassc "0.10.4"]
                       [lein-auto "0.1.1"]
                       [lein-environ "1.0.2"]]

  :source-paths       ["src/bo" "src/common" "src/fo" "dev"]
  :test-paths         ["test/bo"]
  :min-lein-version   "2.5.3"
  :clean-targets      ^{:protect false} [:target-path :compile-path "resources/public/js"]
  :uberjar-name       "{{{name}}}.jar"
  :jar-exclusions     [#".*\.sw?$" #"resources/public/js/compiled/out/"]
  :uberjar-exclusions [#".*\.sw?$" #"resources/public/js/compiled/out/"]


  :main               {{{project-ns}}}.main
  :repl-options       {:init-ns user
                       :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}


  :cljsbuild          {:builds
                       {:app
                        {:source-paths ["src/fo" "src/common"]

                         :figwheel {:on-jsload "{{{project-ns}}}.figwheel/on-reload"}

                         :compiler {:main                 {{{project-ns}}}.core
                                    :asset-path           "/js/compiled/out"
                                    :output-to            "resources/public/js/compiled/{{{sanitized}}}.js"
                                    :output-dir           "resources/public/js/compiled/out"
                                    :source-map-timestamp true
                                    :optimizations        :none
                                    :parallel-build       true
                                    :pretty-print         true}}}}

  :figwheel           {:css-dirs       ["resources/public/css"]
                       :ring-handler   user/ring-handler
                       :server-logfile "log/figwheel.log"}

  :doo                {:build "test"}
  :codox              {:project  {:name "{{name}}"}
                       :defaults {:doc/format :markdown}
                       :language :clojurescript}

  :sassc              [{:src       "src/scss/app.scss"
                        :output-to "resources/public/css/app.css"}]

  :auto               {"sassc" {:file-pattern  #"\.(scss)$"}}

  :profiles           {:dev
                       {:dependencies [[midje "1.8.3"]
                                       [peridot "0.4.3"]
                                       [kerodon "0.7.0" :exclusions [peridot]]
                                       [org.clojure/test.check "0.9.0"]
                                       [figwheel "0.5.0-6"]
                                       [figwheel-sidecar "0.5.0-6"]
                                       [com.cemerick/piggieback "0.2.1"]
                                       [org.clojure/tools.nrepl "0.2.12"]]
                        :jvm-opts      ["-XX:+PerfDisableSharedMem"
                                        "-Dstorage.diskCache.bufferSize=50"
                                        "-Dstorage.wal.maxSize=15"
                                        "-Ddb.pool.max=3"]

                        :injections    [(use 'midje.repl)
                                        (run)]

                        :plugins       [[lein-figwheel "0.5.0-6"]
                                        [lein-doo "0.1.6"]
                                        [lein-midje "3.2"]]

                        :env           {:dev "true"
{{{project-clj-profiles-env}}}}

                        :cljsbuild     {:builds
                                        {:test
                                         {:source-paths ["src/fo" "src/common" "test/fo"]
                                          :compiler
                                          {:output-to     "resources/public/js/compiled/testable.js"
                                           :main          {{{project-ns}}}.test-runner
                                           :optimizations :none}}}}}

                       :uberjar
                       {:source-paths ^:replace ["src/bo" "src/common"]
                        :hooks                  [leiningen.cljsbuild
                                                 leiningen.sassc]
                        :omit-source            true
                        :aot                    :all
                        :cljsbuild              {:builds
                                                 {:app
                                                  {:source-paths ^:replace ["src/fo" "src/common"]
                                                   :compiler
                                                   {:optimizations :advanced
                                                    :pretty-print  false}}}}}})
