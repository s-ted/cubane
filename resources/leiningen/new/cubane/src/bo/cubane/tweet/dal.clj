(ns {{{project-ns}}}.tweet.dal
  (:require
    [cubanostack.dal :as dal]))



(defn new-dal []
  (dal/new-dal :tweet))
