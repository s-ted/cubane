(ns {{project-ns}}.nav.ui
  (:require
    [cubanostack.helper.ui :as ui :refer-macros [defcomponent h%]]
    [cubanostack.components.bus :as bus]))


(defn- nav! [handler Bus]
  (bus/send! Bus :route! {:handler handler}))

(ui/defcomponent Nav
  [route-info Bus]

  [:ReactBootstrap/Nav {:bsStyle   :tabs
                        :activeKey (:handler route-info)}
   [:ReactBootstrap/NavItem {:eventKey :home
                             :onClick (h% (nav! :home Bus))} "Home"]
   [:ReactBootstrap/NavItem {:eventKey :{{project-ns}}.test-cmp.module/test
                             :onClick (h% (nav! :{{project-ns}}.test-cmp.module/test Bus))} "Test"]
   [:ReactBootstrap/NavItem {:eventKey :{{project-ns}}.chat.module/chat
                             :onClick (h% (nav! :{{project-ns}}.chat.module/chat Bus))} "Chat"]
   [:ReactBootstrap/NavItem {:eventKey :{{project-ns}}.tweet.module/tweet
                             :onClick (h% (nav! :{{project-ns}}.tweet.module/tweet Bus))} "Tweet"]
   [:ReactBootstrap/NavItem {:eventKey :{{project-ns}}.user.module/user
                             :onClick (h% (nav! :cubanostack.user.module/user Bus))} "User"]])
