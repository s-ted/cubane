(ns {{project-ns}}.components.chat-broadcaster
  (:require [com.stuartsierra.component :as c]
            [cubanostack.components.bus :as bus]
            [cubanostack.components.wrapper-manager :as wm]
            [cubanostack.wrapper.core :as w]
            [cubanostack.components.sente :as sente]))


(defprotocol ChatBroadcaster
  (broadcast! [this message]))


(defn- -<client [{:keys [?data]} ChatBroadcaster]
  (broadcast! ChatBroadcaster (:content ?data)))



(defrecord ChatBroadcaster* [SenteCommunicator Bus WrapperManager]
  c/Lifecycle

  (start [this]
    (wm/register WrapperManager :sente.chat/message ::broadcaster
                 (w/handler #(-<client % this)))
    this)

  (stop [this]
    (wm/unregister WrapperManager :sente.chat/message ::broadcaster)
    this)

  ChatBroadcaster

  (broadcast! [this message]
    (doseq [uid (:any (sente/connected-uids SenteCommunicator))]
      (bus/send! Bus :>client!
                 {:uid     uid
                  :topic   :chat/broadcast
                  :payload {:content message}}))))


(defn new-chatBroadcaster []
  (map->ChatBroadcaster* {}))
