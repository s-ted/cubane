(ns {{{project-ns}}}.test-core
  (:require
    clojure.walk
    [clojure.data.json :as json]
    [com.stuartsierra.component :as component]
    [peridot.core :as peridot]
    [{{{project-ns}}}.app :as app]
    [cubanostack.components.handler :as handler]))


(def ^:private test-config nil)

(let [s (atom nil)]
  (defn reuse-system []
    (swap! s
           (fn [system]
             (if system
               system
               (-> (app/system-test test-config)
                   component/start))))))

(defn new-system []
  (-> (app/system-test test-config)
      component/start))

(defn reuse-handler []
  (-> (reuse-system)
      :Handler
      handler/handler))

(defn new-handler []
  (-> (new-system)
      :Handler
      handler/handler))






(defn parse-json-response [response]
  (-> response
      (get-in [:response :body])

      json/read-str
      clojure.walk/keywordize-keys))


(defn entities->ids [handler listing-url]
  (-> handler
      peridot/session
      (peridot/request listing-url)
      parse-json-response
      (#(map :_id %))))
