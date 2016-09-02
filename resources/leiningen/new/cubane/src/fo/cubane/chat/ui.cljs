(ns {{project-ns}}.chat.ui
  (:require
    [clojure.string :as str]
    [cubanostack.helper.ui :as ui :refer-macros [defcomponent h%]]
    [cubanostack.components.bus :as bus]))

(def item-state-prefix [::local-chat-message])
(def list-state-prefix [:chat-messages])

(defn- set-local-message [message Bus]
  (doto Bus
    (bus/send! :state/store! {:id-path item-state-prefix
                              :value   message})
    (bus/send! :renderer)))

(defn- send-chat-message! [content Bus]
  (when-not (str/blank? content)
    (bus/send! Bus :>server! {:topic :chat/message :payload {:content content}})
    (set-local-message "" Bus)))


(ui/defcomponent Messages
  [messages Bus]

  [:ReactBootstrap/ListGroup nil
   (map (fn [message]
          [:ReactBootstrap/ListGroupItem {:key (hash message)}
           message])
        messages)])

(ui/defcomponent Sender
  [local-message Bus]

  [:ReactBootstrap/Panel nil
   [:form {:className "form-horizontal"}
    [:ReactBootstrap/FormGroup {:controlId ">"}
     [:ReactBootstrap/ControlLabel nil ">"]
     [:ReactBootstrap/FormControl
      {:type             "text"
       :value            local-message
       :placeholder      "Your message..."
       :onChange
       (h%
         (set-local-message
           (-> event
               .-target
               .-value)
           Bus))}]]

    [:ReactBootstrap/Button
     {:bsStyle   "primary"
      :type      "submit"
      :className "col-xs-offset-2"
      :onClick
      (h%
        (.preventDefault event)
        (send-chat-message!
          local-message
          Bus))}
     [:ReactBootstrap/Glyphicon {:glyph :send}]
     " Send"]]])

(ui/defcomponent UI
  [[listing item] Bus]

  [:div nil

   (Sender item Bus)

   (Messages listing Bus)])
