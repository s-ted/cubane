(ns {{project-ns}}.components.home
  (:require [com.stuartsierra.component :as c]
            [clojure.java.io :as io]
            [cubanostack.components.bus :as bus]))



(def always-index-html
  {:status  200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (slurp
              (io/resource "public/index.html"))})



(defrecord Home [Bus WrapperManager]
  c/Lifecycle

  (start [this]
    (bus/send! Bus :router/set-match-all-route {:handler (constantly always-index-html)})

    this)

  (stop [this]
    (bus/send! Bus :router/set-match-all-route nil)
    this))


(defn new-home []
  (map->Home {}))
