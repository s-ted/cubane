(ns {{project-ns}}.home.ui
  (:require
    [cubanostack.helper.ui :as ui :refer-macros [defcomponent]]))

(ui/defcomponent UI
  [state]

  [:ReactBootstrap/Panel nil
   [:ReactBootstrap/PageHeader nil
    (:text state)
    [:small nil (str (get-in state [:route-info :handler]))]]
   [:ReactBootstrap/Button {:bsStyle :success} "Click me!"]
   [:p nil
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse elementum odio in facilisis consectetur. Donec mollis ornare dui. Integer risus elit, sollicitudin ac placerat sit amet, congue id nisl. Sed luctus massa non vulputate rutrum. Fusce ac metus lobortis, placerat leo sed, eleifend ipsum. Ut volutpat pharetra mi vitae facilisis. Mauris pharetra sodales ex vel luctus. Aenean vehicula ultrices viverra. Nunc pulvinar nisi non purus fringilla iaculis."]])
