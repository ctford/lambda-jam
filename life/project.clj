(defproject life "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[midje "1.6-alpha2"]]
                   :plugins  [[lein-midje "3.0.1"]]}})
  
