(ns user
  (:require [{{project-ns}}.app :as app]
            [figwheel-sidecar.system :as figwheel]
            [com.stuartsierra.component :as component]
            [cubanostack.components.handler :as handler]
            [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.java.shell]))

;; Let Clojure warn you when it needs to reflect on types, or when it does math
;; on unboxed numbers. In both cases you should add type annotations to prevent
;; degraded performance.
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(defn start-sass []
  (future
    (println "Starting sass.")
    (clojure.java.shell/sh "lein" "auto" "sassc" "once")))










(def system nil)
(def ring-handler nil)


(defn init []
  (alter-var-root #'system
                  (constantly
                    (app/system-dev
                      :figwheel-system   (figwheel/figwheel-system (figwheel/fetch-config))
                      :css-watcher       (figwheel/css-watcher {:watch-paths ["resources/public/css"]})))))

(defn start []
  (alter-var-root #'system
                  component/start))

(defn stop []
  (alter-var-root #'system
                  (fn [s]
                    (when s
                      (component/stop s)))))

(defn go []
  (init)
  (start)
  (alter-var-root #'ring-handler
                  (constantly
                    (handler/handler (get-in system [:Handler])))))
(defn reset []
  (stop)
  (refresh :after 'user/go))



(defn run []
  ;(start-sass)               ; TODO: componentize this

  (go))


(defn browser-repl []
  (figwheel/cljs-repl (:figwheel-system system)))
