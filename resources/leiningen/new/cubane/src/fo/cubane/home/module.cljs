(ns {{project-ns}}.home.module
  (:require
    [com.stuartsierra.component :as c]
    [cubanostack.components.bus :as bus]
    [cubanostack.components.wrapper-manager :as wm]
    [cubanostack.wrapper.core :as w]
    [{{project-ns}}.home.ui :as ui]
    [{{project-ns}}.templates.ui :as templates-ui]))




(defrecord Renderer [route]
  w/Wrapper

  (before [this payload]
    payload)

  (after [this response {:keys [state] :as payload}]
    (if (= route
           (get-in state [:route-info :handler]))
      (assoc-in response
                [:template-args :center] (ui/UI state))
      response)))


(defrecord Module [id route ->Wrapper Bus WrapperManager]
  c/Lifecycle

  (start [this]
    (wm/register WrapperManager :render-state id
                 (->Wrapper id))
    (bus/send! Bus :router/add-route {:route   route
                                      :handler id})
    (bus/send! Bus :renderer/add-template {:handle       :default
                                           :ui-component templates-ui/DefaultTemplate})
    this)

  (stop [this]
    (bus/send! Bus :router/remove-route {:route route})
    (wm/unregister WrapperManager :render-state id)
    this))


(defn new-home
  ([]
   (new-home [""]))

  ([route]
    (map->Module {:id :home :route route :->Wrapper ->Renderer})))
