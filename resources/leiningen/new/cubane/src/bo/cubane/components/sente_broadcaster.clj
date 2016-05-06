(ns {{project-ns}}.components.sente-broadcaster
  (:require [com.stuartsierra.component :as c]
            [cubanostack.components.bus :as bus]
            [cubanostack.components.sente :as sente]
            [clojure.core.async :as async :refer (<! go-loop)]))

(defn- start-example-broadcaster!
  "As an example of server>user async pushes, setup a loop to broadcast an
  event to all connected users every 60 seconds"
  [senteCommunicator Bus]
  (let [broadcast!
        (fn [i]
          ;(println "Broadcasting server>user:" @connected-uids)
          (doseq [uid (:any (sente/connected-uids senteCommunicator))]
            (bus/send! Bus :>client!
                       {:uid        uid
                        :topic      :chat/broadcast
                        :payload    {:content      (str "Async broadcast pushed to id " uid
                                                        " from server (every 60 seconds) #" i)
                                     :what-is-this "An async broadcast pushed from server"
                                     :how-often    "Every 60 seconds"
                                     :to-whom      uid
                                     :i            i}})))]

    (go-loop [i 0]
             (<! (async/timeout 60000))
             (broadcast! i)
             (recur (inc i)))))


(defprotocol SenteBroadcaster
  (start [this])
  (stop [this]))


(defrecord SenteBroadcaster* [SenteCommunicator Bus]
  c/Lifecycle

  SenteBroadcaster

  (start [this]
    (cond-> this
      (nil? (::broadcaster this))
      (assoc ::broadcaster
             (future (start-example-broadcaster! SenteCommunicator Bus)))))

  (stop [this]
    (when (future? (::broadcaster this))
      (deref (::broadcaster this) 0 nil))
    (assoc this ::broadcaster nil)))


(defn new-senteBroadcaster []
  (map->SenteBroadcaster* {}))
