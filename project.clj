(defproject striker "0.1.0"
  :description "Draw lines on PDF documents"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.3.1"]
                 [com.itextpdf/itextpdf "5.4.4"]]
  :main striker.core
  :aot :all)
