(ns {{{project-ns}}}.integrated.tweet-test
  (:require
    [{{{project-ns}}}.tweet.model :as model]
    [{{{project-ns}}}.integrated.crud :as crud]
    [midje.sweet :refer :all]))


(facts
  "tweet component"

  (facts
    "RESTFull API"

    (crud/test-crud "/app/tweet/"
                    model/PrivateRead
                    model/PrivateWrite)))
