(ns {{project-ns}}.system
  "This is the main namespace for the **{{name}} FO** (cl)JS system declaration"

  (:require
    [com.stuartsierra.component :refer [system-map using] :as component]
    [cubanostack.app.ui :as ui]
    [cubanostack.components.bus :as bus]
    [cubanostack.components.config :as config]
    [cubanostack.components.renderer :as renderer]
    [cubanostack.components.router :as router]
    [cubanostack.components.sente :as sente]
    [cubanostack.components.state :as state]
    [cubanostack.components.wrapper-manager :as wrapper-manager]
    [cubanostack.dashboard.module :as dashboard]
    [cubanostack.notification.module :as notification]
    [cubanostack.settings.wrapper.module :as setting-wrapper]
    [cubanostack.settings.router.module :as setting-router]
    [cubanostack.user.login.module :as user-login-module]
    [cubanostack.user.login.rest :as user-login-rest]
    [cubanostack.user.module :as user-module]
    [cubanostack.user.rest :as user-rest]

    [{{project-ns}}.chat.module :as chat]
    [{{project-ns}}.home.module :as home]
    [{{project-ns}}.tweet.module :as tweet-module]
    [{{project-ns}}.tweet.rest :as tweet-rest]
    [{{project-ns}}.test-cmp.module :as test-cmp]))


(defn system-definition []
  (system-map
    :State  (state/new-state
              {:text "Hello {{name}} by Cubane!"
               :notifications {(gensym) {:content "Bienvenu sur {{name}}!"
                                         :action  #(js/console.debug "action!")
                                         :label   "Do it"}}})
    :Config (using
               (config/new-config)
               [:State])
    :WrapperManager (using
                      (wrapper-manager/new-wrapperManager)
                      [:State])
    :Bus      (using
                (bus/new-bus)
                [:WrapperManager])
    :Renderer (using
                (renderer/new-renderer ui/NotFoundComponent)
                [:WrapperManager :State :Bus])
    :Router   (using
                (router/new-router)
                [:State :Bus :WrapperManager])
    :HomePage (using
                (home/new-home)
                [:Bus :WrapperManager
                 :Router])
    :TestPage (using
                (test-cmp/new-test-page)
                [:Bus :WrapperManager
                 :Router])
    :Sente    (using
                (sente/new-sente)
                [:Bus :WrapperManager])
    :Chat     (using
                (chat/new-chat)
                [:Bus :WrapperManager
                 :Router])
    :NotificationManager (using
                           (notification/new-notification)
                           [:Bus :WrapperManager])
    :Dashboard    (using
                    (dashboard/new-dashboard)
                    [:Bus :WrapperManager
                     :Router])
    :WrapperSetting (using
                      (setting-wrapper/new-wrapperSetting)
                      [:Bus :WrapperManager
                       :Dashboard])
    :RouterSetting (using
                     (setting-router/new-routerSetting)
                     [:Bus :WrapperManager
                      :Dashboard])


    :UserRest      (using
                     (user-rest/new-rest)
                     [:Bus :State])
    :UserModule    (using
                     (user-module/new-userModule)
                     [:Bus :WrapperManager :UserRest
                      :Router])

    :UserLoginRest   (using
                       (user-login-rest/new-rest)
                       [:Bus :State])
    :UserLoginModule (using
                       (user-login-module/new-userLoginModule)
                       [:Bus :WrapperManager :UserLoginRest
                        :Router])

    :TweetRest     (using
                     (tweet-rest/new-rest)
                     [:Bus :State])
    :TweetModule   (using
                     (tweet-module/new-tweetModule)
                     [:Bus :WrapperManager :TweetRest
                      :Router])

    ))
