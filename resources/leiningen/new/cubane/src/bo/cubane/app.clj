(ns {{project-ns}}.app
  (:require
    [environ.core :as environ]
    [com.stuartsierra.component :refer [system-map using] :as component]
    [cubanostack.components.bus :as bus]
    [cubanostack.components.config :as config]
    [cubanostack.components.handler :as handler]
    [cubanostack.components.middleware :as middleware]
    [cubanostack.components.router :as router]
    [cubanostack.components.sente :as sente]
    [cubanostack.components.state :as state]
    [cubanostack.components.web-server :as web-server]
    [cubanostack.components.wrapper-manager :as wrapper-manager]
    [cubanostack.sitemap.service :as sitemap-service]
    [cubanostack.user.service :as user-service]
    [cubanostack.user.dal :as user-dal]
    [cubanostack.user.login.service :as user-login-service]
    [cubanostack.server :as server]

    [{{project-ns}}.components.sente-broadcaster :as sente-broadcaster]
    [{{project-ns}}.components.home :as home]
    [{{project-ns}}.components.fixtures :as fixtures]
    [{{project-ns}}.components.chat-broadcaster :as chat-broadcaster]
    [{{project-ns}}.tweet.service :as tweet-service]
    [{{project-ns}}.tweet.dal :as tweet-dal]
{{{app-clj-requires}}}))


(defn system-prod [http-port]
  (system-map
    :State             (state/new-state)
    :Config            (using
                         (config/new-config)
                         [:State])
    :WrapperManager    (using
                         (wrapper-manager/new-wrapperManager)
                         [:State])
    :Bus               (using
                         (bus/new-bus)
                         [:WrapperManager])

    {{#orient-db?}}
    :GenericDal        (orientdb/new-dal
                         {:store (environ/env :orient-db-store)})
    {{/orient-db?}}

    :Router            (using
                         (router/new-router)
                         [:Bus :State :WrapperManager])
    :Middleware        (middleware/new-middleware
                         {:middleware (server/middlewares)})
    :Handler           (using
                         (handler/new-handler)
                         [:Router :Middleware])
    :WebServer         (using
                         (web-server/new-web-server http-port)
                         [:Handler])

    :SenteCommunicator (using
                         (sente/new-senteCommunicator)
                         [:Bus :WrapperManager
                          :Router])
    :SenteBroadcaster  (using
                         (sente-broadcaster/new-senteBroadcaster)
                         [:Bus :SenteCommunicator])
    :ChatBroadcaster   (using
                         (chat-broadcaster/new-chatBroadcaster)
                         [:Bus :WrapperManager :SenteCommunicator])
    :Home              (using
                         (home/new-home)
                         [:Bus :WrapperManager
                          :Router])))

(defn system-dev [& components]
  (apply system-map
         :State             (state/new-state)
         :Config            (using
                              (config/new-config)
                              [:State])
         :WrapperManager    (using
                              (wrapper-manager/new-wrapperManager)
                              [:State])
         :Bus               (using
                              (bus/new-bus)
                              [:WrapperManager])

         {{#orient-db?}}
         :GenericDal       (orientdb/new-dal
                              {:store (environ/env :orient-db-store)})
         {{/orient-db?}}


         :Router            (using
                              (router/new-router)
                              [:Bus :State :WrapperManager])
         :Middleware        (middleware/new-middleware
                              {:middleware (server/middlewares)})
         :Handler           (using
                              (handler/new-handler)
                              [:Router :Middleware])

         :SenteCommunicator (using
                              (sente/new-senteCommunicator)
                              [:Bus :WrapperManager
                               :Router])
         :SenteBroadcaster  (using
                              (sente-broadcaster/new-senteBroadcaster)
                              [:Bus :SenteCommunicator])
         :ChatBroadcaster   (using
                              (chat-broadcaster/new-chatBroadcaster)
                              [:Bus :WrapperManager :SenteCommunicator])

         :Home              (using
                              (home/new-home)
                              [:Bus :WrapperManager
                               :Router])

         :SitemapService    (using
                              (sitemap-service/new-sitemapService)
                              [:Bus :Router])

         :UserDal           (using
                               (user-dal/new-dal)
                               [:GenericDal])
         :UserService       (using
                              (user-service/new-userService)
                              [:Bus :UserDal
                               :Router])
         :UserLoginService  (using
                               (user-login-service/new-userLoginService)
                               [:Bus :UserDal
                                :Router])

         :TweetDal          (using
                              (tweet-dal/new-dal)
                              [:GenericDal])
         :TweetService      (using
                              (tweet-service/new-tweetService)
                              [:Bus :TweetDal
                               :Router])

         :Fixtures          (using
                              (fixtures/new-fixtures)
                              [:TweetDal :UserDal])

         components))

(defn system-test [config & components]
  (apply system-map
         :State             (state/new-state)
         :Config            (using
                              (config/new-config config)
                              [:State])
         :WrapperManager    (using
                              (wrapper-manager/new-wrapperManager)
                              [:State])
         :Bus               (using
                              (bus/new-bus)
                              [:WrapperManager])

         {{#orient-db?}}
         :GenericDal       (orientdb/new-dal
                              {:store "memory:test"
                               :empty-at-start? true})
         {{/orient-db?}}


         :Router            (using
                              (router/new-router)
                              [:Bus :State :WrapperManager])
         :Middleware        (middleware/new-middleware
                              {:middleware (server/middlewares)})
         :Handler           (using
                              (handler/new-handler)
                              [:Router :Middleware])

         :SenteCommunicator (using
                              (sente/new-senteCommunicator)
                              [:Bus :WrapperManager
                               :Router])
         :SenteBroadcaster  (using
                              (sente-broadcaster/new-senteBroadcaster)
                              [:Bus :SenteCommunicator])
         :ChatBroadcaster   (using
                              (chat-broadcaster/new-chatBroadcaster)
                              [:Bus :WrapperManager :SenteCommunicator])

         :Home              (using
                              (home/new-home)
                              [:Bus :WrapperManager
                               :Router])

         :SitemapService    (using
                              (sitemap-service/new-sitemapService)
                              [:Bus :Router])

         :UserDal           (using
                              (user-dal/new-dal)
                              [:GenericDal])
         :UserService       (using
                              (user-service/new-userService)
                              [:Bus :UserDal
                               :Router])
         :UserLoginService  (using
                              (user-login-service/new-userLoginService)
                              [:Bus :UserDal
                               :Router])

         :TweetDal          (using
                              (tweet-dal/new-dal)
                              [:GenericDal])
         :TweetService      (using
                              (tweet-service/new-tweetService)
                              [:Bus :TweetDal
                               :Router])

         components))
