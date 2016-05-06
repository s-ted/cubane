(ns {{project-ns}}.core
  "This is the main namespace for the **{{name}} FO** (cl)JS application"

  (:require
    [com.stuartsierra.component :refer [system-map using] :as component]
    [cubanostack.components.bus :as bus]
    [{{project-ns}}.system :as s]))


(defonce system (atom nil))

(defn- init []
  (reset! system
    (s/system-definition)))

(defn start []
  (swap! system component/start))

(defn stop []
  (swap! system
         (fn [s]
           (when s
             (component/stop s)))))

(defn
  ^{:export true
    :doc    "This is the **main entry point** of the javascript application."
    :added  "1.0"}
  main

  []

  (try
    (init)
    (start)

    (doto (:Bus @system)
      (bus/send! :route-path! {:path (str js/window.location.pathname js/window.location.search)})
      (bus/send! :renderer {:route-changed? true}))

    (catch :default e
      (.error js/console (clj->js e)))))


(when (nil? @system)
  (do
    (enable-console-print!)
    (main)))
