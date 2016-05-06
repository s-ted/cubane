(ns {{project-ns}}.components.fixtures
  (:require [com.stuartsierra.component :as c]
            [buddy.hashers :as hashers]
            [cubanostack.dal :as dal]))

(defrecord Module* [TweetDal UserDal]
  c/Lifecycle

  (start [this]
    (dal/create-with-id! UserDal
                         "5f852c06-ed1e-11e5-9a13-898cdc1050e5"
                         {:username "admin"
                          :password (hashers/encrypt "password")
                          :roles    ["admin"]})
    (dal/create-with-id! UserDal
                         "71387516-ed1e-11e5-a787-898cdc1050e5"
                         {:username "user"
                          :password (hashers/encrypt "password")
                          :roles    ["user"]})

    (dal/create-with-id! TweetDal
                         "8539afb2-ed1e-11e5-98a8-898cdc1050e5"
                         {:author "fixtures"
                          :content "The testing tweet"})

    this)

  (stop [this]
    (dal/delete! UserDal "5f852c06-ed1e-11e5-9a13-898cdc1050e5")
    (dal/delete! UserDal "71387516-ed1e-11e5-a787-898cdc1050e5")

    (dal/delete! TweetDal "8539afb2-ed1e-11e5-98a8-898cdc1050e5")

    this))


(defn new-fixtures []
  (map->Module* {}))
