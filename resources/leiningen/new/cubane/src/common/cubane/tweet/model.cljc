(ns {{project-ns}}.tweet.model
  (:require
    [cubanostack.model :as model]
    #?(:clj  [schema.core :as s :refer [defschema]]
       :cljs [schema.core :as s :include-macros true :refer-macros [defschema]])))



(defschema PublicRead
  (merge model/EntitySchema
         {:content s/Str
          :author  s/Str}))

(defschema PublicWrite
  {})

(defschema PrivateRead
  (merge PublicRead
         {}))

(defschema PrivateWrite
  (merge PublicWrite
         {:content s/Str}))

(defschema Persisted
  (merge PrivateWrite
         {(s/optional-key :author) s/Str}))
