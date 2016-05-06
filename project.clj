(defproject cubane/lein-template "0.1.0-SNAPSHOT"
  :description  "A Leiningen template for a ClojureScript setup with Figwheel, Quiescient."
  :url          "http://github.com/s-ted/cubane"
  :author       "Sylvain Tedoldi"
  :license      {:name "Eclipse Public License"
                 :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true


  :dependencies [[com.github.plexus/clj-jgit "v0.8.9-preview"]
                 [org.slf4j/slf4j-nop "1.7.14"]]

  :repositories [["jitpack" "https://jitpack.io"]])
