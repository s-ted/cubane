(ns {{project-ns}}.figwheel
  (:require
    [cubanostack.components.bus :as bus]
    [{{project-ns}}.core :as core]))

(defn on-reload []
  (bus/send! (:Bus @core/system) :renderer))
