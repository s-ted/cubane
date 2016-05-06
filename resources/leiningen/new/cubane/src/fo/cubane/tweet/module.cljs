(ns {{project-ns}}.tweet.module
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [com.stuartsierra.component :as c]
    [cljs.core.async :as async]
    [cubanostack.helper.listing :as listing]
    [cubanostack.components.bus :as bus]
    [cubanostack.components.router :as router]
    [cubanostack.components.wrapper-manager :as wm]
    [cubanostack.rest :as rest]
    [cubanostack.wrapper.core :as w]
    [{{project-ns}}.tweet.ui :as ui]))



(defn- -send! [{:keys [content]} Bus TweetRest]
  (go
    (async/<! (rest/create! TweetRest {:content content}))
    (listing/refresh-list Bus TweetRest)))





(defrecord Renderer [route TweetRest]
  w/Wrapper

  (before [this payload]
    payload)

  (after [this response {:keys [route-changed? state Bus]}]
    (if (= route
           (get-in state [:route-info :handler]))
      (do
        (when route-changed?
          (bus/send! Bus :tweet/refresh-list))
        (assoc-in response
                  [:template-args :center]
                  (ui/UI
                    [(get-in state ui/list-state-prefix)
                     (get-in state ui/item-state-prefix)]
                    Bus)))
      response)))

(defrecord Module [id route ->Wrapper Bus WrapperManager TweetRest]
  c/Lifecycle

  (start [this]
    (wm/register WrapperManager :tweet/refresh-list id
                 (w/handler (fn [_] (listing/refresh-list Bus TweetRest))))
    (wm/register WrapperManager :tweet/send! id
                 (w/handler #(-send! % Bus TweetRest)))
    (wm/register WrapperManager :render-state id
                 (->Wrapper id TweetRest))
    (bus/send! Bus :router/add-route {:route   route
                                      :handler id})
    this)

  (stop [this]
    (bus/send! Bus :router/remove-route {:route route})
    (wm/unregister WrapperManager :render-state id)
    (wm/unregister WrapperManager :tweet/send! id)
    (wm/unregister WrapperManager :tweet/refresh-list id)
    this))



(defn new-tweetModule
  ([]
   (new-tweetModule ["tweet"]))

  ([route]
   (map->Module {:id ::tweet :route route :->Wrapper ->Renderer})))
