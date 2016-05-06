(ns {{project-ns}}.tweet.rest
  (:require
    [cubanostack.rest :as rest]))


(defn new-rest []
  (rest/new-rest {:url "app/tweet/"}))
