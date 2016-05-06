(ns {{project-ns}}.chat.module
  (:require
    [com.stuartsierra.component :as c]
    [cubanostack.components.bus :as bus]
    [cubanostack.components.router :as router]
    [cubanostack.components.sente :as sente]
    [cubanostack.components.wrapper-manager :as wm]
    [cubanostack.wrapper.core :as w]
    [{{project-ns}}.chat.ui :as ui]))




(defn- -<server [[event payload] Bus]
  (when (= :chat/broadcast event)
    (doto Bus
      (bus/send! :state/update! {:id-path [:chat-messages]
                                 :f       #(concat % [(:content payload)])})
      (bus/send! :renderer))))


(defrecord Renderer [route]
  w/Wrapper

  (before [this payload]
    payload)

  (after [this response {:keys [state Bus] :as payload}]
    (if (= route
           (get-in state [:route-info :handler]))
      (assoc-in response
                [:template-args :center]
                (ui/UI
                  [(get-in state ui/list-state-prefix)
                   (get-in state ui/item-state-prefix)]
                  Bus))
      response)))

(defrecord Module [id route ->Wrapper Bus WrapperManager]
  c/Lifecycle

  (start [this]
    (wm/register WrapperManager :<server id
                 (w/handler #(-<server % Bus)))
    (wm/register WrapperManager :render-state id
                 (->Wrapper id))
    (bus/send! Bus :router/add-route {:route   route
                                      :handler id})
    this)

  (stop [this]
    (bus/send! Bus :router/remove-route {:route route})
    (wm/unregister WrapperManager :render-state id)
    (wm/unregister WrapperManager :<server id)
    this))



(defn new-chat
  ([]
   (new-chat ["chat"]))

  ([route]
   (map->Module {:id ::chat :route route :->Wrapper ->Renderer})))
