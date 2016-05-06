(ns {{project-ns}}.tweet.service
  (:require [com.stuartsierra.component :as c]
            [liberator.core :refer [defresource]]
            [schema.coerce :as coerce]
            [schema-tools.core :as schema]
            [{{project-ns}}.tweet.model :as model]
            [cubanostack.components.bus :as bus]
            [cubanostack.crud :as crud]
            [cubanostack.dal :as dal]))





(defn map->entity [patch? m]
  (-> m
      (schema/select-schema
        model/Persisted
        coerce/json-coercion-matcher)))

(defn entity->map [e]
  (-> {:author "anonymous"}
      (merge e)
      (schema/select-schema
        model/PrivateRead
        coerce/json-coercion-matcher)))

(defn- ctx->entity-id [ctx]
  (get-in ctx [:request :route-params :id]))


(defresource list-resource [TweetDal]
  (crud/default-collection-liberator-config
    map->entity
    entity->map
    TweetDal))

(defresource entity-resource [TweetDal]
  (crud/default-item-liberator-config
    map->entity
    entity->map
    ctx->entity-id
    TweetDal))



(defrecord Service* [route-path Bus TweetDal]
  c/Lifecycle

  (start [this]
    (doto Bus
      (bus/send! :router/add-route {:route   (conj route-path "")
                                    :handler (list-resource TweetDal)})
      (bus/send! :router/add-route {:route   (conj route-path [:id ""])
                                    :handler (entity-resource TweetDal)}))
    this)

  (stop [this]
    (doto Bus
      (bus/send! :router/remove-route {:route (conj route-path [:id ""])})
      (bus/send! :router/remove-route {:route (conj route-path "")}))
    this))


(defn new-tweetService []
  (map->Service* {:route-path ["app/" "tweet/"]}))
