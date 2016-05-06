(ns {{project-ns}}.templates.ui
  (:require
    [cubanostack.helper.ui :as ui :refer-macros [defcomponent h%]]
    [cubanostack.components.bus :as bus]
    [cubanostack.dashboard.ui :as dashboard]
    [cubanostack.notification.ui :as notification]
    [cubanostack.local-storage :as lstorage]
    [{{project-ns}}.nav.ui :as nav]))

(defn- -nav! [handler Bus]
  (bus/send! Bus :route! {:handler handler}))


(ui/defcomponent DefaultTemplate
  [[{:keys [header menu center footer]}
    {:keys [route-info ui-options notifications]
     :as state}]
   Bus]

  (let [current-user (lstorage/get-item-from-local-storage :current-user)

        header
        (or header
            [:ReactBootstrap/Navbar {:inverse true}
             [:ReactBootstrap.Navbar/Header nil
              [:ReactBootstrap.Navbar/Brand nil
               [:a {:href "#"
                    :onClick (h% (-nav! :home Bus))}
                [:b nil "Cubane"]
                "{{name}}"]]
              [:ReactBootstrap.Navbar/Toggle nil]]
             [:ReactBootstrap.Navbar/Collapse nil
              (nav/Nav route-info Bus)

              [:ReactBootstrap/Nav {:pullRight true}
               (if-let [current-user current-user]
                 [:ReactBootstrap/NavItem {:onClick (h% (-nav! :profile Bus))}
                  [:ReactBootstrap/Glyphicon {:glyph :user}]
                  " " (:username current-user)]
                 ;else
                 [:ReactBootstrap/NavItem {:onClick (h% (-nav! :login Bus))}
                  [:ReactBootstrap/Glyphicon {:glyph :user}]])

               [:ReactBootstrap/NavItem {:onClick (h% (-nav! :dashboard Bus))}
                [:ReactBootstrap/Glyphicon {:glyph :cog}]]

               (notification/UI [ui-options notifications] Bus)]]])

        footer
        (or footer
            [:div {:className "footer"}
             "© 2016 {{name}} by Cubane"])]

    [:div {:className "wrapper"}

     header

     (if menu
       [:div {:fluid true}
        [:ReactBootstrap/Row nil
         [:ReactBootstrap/Col {:lg 2 :sm 3} menu]
         [:ReactBootstrap/Col {:lg 10 :sm 9 :className "center-with-menu"}
          center
          footer]]]
       [:div {:fluid true
              :className "center"}
        center
        footer])]))
