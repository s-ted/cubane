(ns {{project-ns}}.test-cmp.module
  (:require
    [com.stuartsierra.component :as c]
    [cubanostack.module :as module]
    [cubanostack.wrapper.core :as w]
    [{{project-ns}}.test-cmp.ui :as ui]))




(defrecord Renderer [route]
  w/Wrapper

  (before [this payload]
    payload)

  (after [this response {:keys [state] :as payload}]
    (if (= route
           (get-in state [:route-info :handler]))
      (assoc-in response
                [:template-args :center] (ui/UI state))
      response)))


(defn new-test-page
  ([]
   (new-test-page ["test"]))

  ([route]
    (module/new-module ::test route ->Renderer)))
