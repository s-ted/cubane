(ns {{project-ns}}.main
  (:require [com.stuartsierra.component :as component]
            [environ.core :refer [env]]

            [{{project-ns}}.app :as app])
  (:gen-class))

(defn -main [& [port]]

  (let [port (Integer/parseInt (or port (env :port) "10555"))]
    (component/start
      (app/system-prod port))))
