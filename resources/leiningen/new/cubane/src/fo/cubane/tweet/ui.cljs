(ns {{project-ns}}.tweet.ui
  (:require
    [clojure.string :as str]
    [cubanostack.helper.ui :as ui :refer-macros [defcomponent h%]]
    [cubanostack.components.bus :as bus]))


(def item-state-prefix [::local-tweet])
(def list-state-prefix [:listing])


(defn- set-local-tweet [message Bus]
  (doto Bus
    (bus/send! :state/store! {:id-path item-state-prefix
                              :value   message})
    (bus/send! :renderer)))

(defn- send-tweet! [content Bus]
  (when-not (str/blank? content)
    (bus/send! Bus :tweet/send! {:content content})
    (set-local-tweet "" Bus)))

(defn- refresh-tweets [Bus]
  (bus/send! Bus :tweet/refresh-list))

(ui/defcomponent Message
  [{:keys [_id author content createdAt]}
   Bus]

  [:ReactBootstrap/ListGroupItem {:key _id}
   [:.muted author " " createdAt]
   [:p content]])

(ui/defcomponent Messages
  [messages Bus]

  [:ReactBootstrap/ListGroup nil
   (map #(Message % Bus)
        messages)])

(ui/defcomponent Sender
  [local-tweet Bus]

  [:ReactBootstrap/Panel nil
   [:form {:className "form-horizontal"}
    [:ReactBootstrap/Input
     {:type             "text"
      :value            local-tweet
      :placeholder      "Your tweet..."
      :addonBefore      ">"
      :onChange
      (h%
        (set-local-tweet
          (-> event
              .-target
              .-value)
          Bus))}]

    [:ReactBootstrap/Button
     {:bsStyle   "primary"
      :type      "submit"
      :className "col-xs-offset-2"
      :onClick
      (h%
        (.preventDefault event)
        (send-tweet!
          local-tweet
          Bus))}
     [:ReactBootstrap/Glyphicon {:glyph :send}]
     " Send"]]])

(ui/defcomponent UI
  [[listing item]
   Bus]

  [:div nil

   (Sender item Bus)

   [:ReactBootstrap/Button
    {:bsStyle   :default
     :onClick
     (h%
       (refresh-tweets Bus))}
    [:ReactBootstrap/Glyphicon {:glyph :refresh}]
    " Refresh"]


   (Messages listing Bus)])
