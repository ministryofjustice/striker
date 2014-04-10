(ns striker.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [clojure.java.io :as io]
            [cheshire.core :refer [parse-string]])
  (:import (com.itextpdf.text.pdf AcroFields PdfReader PdfStamper)
           (java.util Set)
           (java.io FileOutputStream))
  (:gen-class))


(defn make-reader [pdf-file]
  "get PDF file reader"
  (PdfReader. pdf-file))

(defn make-writer [reader file-name]
  "create PDF file writer"
  (PdfStamper. reader (FileOutputStream. file-name)))

(defn get-page [writer page]
  "get a particular page from a PDF document"
  (.getOverContent writer page))

(defn draw-line [& {:keys [content x y x1 y1 thickness]}]
  "draw a line at given coordinates"
    (.setLineWidth content thickness)
    (.moveTo content x y)
    (.lineTo content (+ x x1) (+ y y1))
    (.stroke content))

(defn display-options [options]
  (println options))

(def cli-options
  [["-h" "--help"]
   ["-j" "--json JSON-FILE" "JSON file with name mappings"
    :default ""
    :parse-fn #(io/file %)
    :validate [#(.isFile %) "name should be provided"]]
   ["-i" "--input INTPUT-PDF" "Template PDF file to start with"
    :default ""
    :parse-fn #(str (read-string %))]
   ["-o" "--output OUTPUT-PDF" "Name under which the new PDF file will be saved"
    :default ""
    :parse-fn #(str (read-string %))]])

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn read-json [json-file]
  "read in a JSON file with form field name mappings, where the key is
   current field name and the value is the new name, exit if the content
   isn't valid JSON'"
  (try
    (parse-string (slurp json-file) true)
    (catch com.fasterxml.jackson.core.JsonParseException e
      (exit 1 (error-msg ["not a proper JSON file"])))))

(defn strike-out [options]
  "take options which will contain INPUT & OUTPUT files as well as
  x,y,x1,y1 coordiantes along with the line thickness that should be
  drawn/struck-out in a INPUT PDF file"
  (let [reader (make-reader (:input options))
        writer (make-writer reader (:output options))
        strikes (:strikes (read-json (:json options)))]
    (doseq [strike strikes]
      (draw-line :content (get-page writer (:page strike))
                 :x (:x strike)
                 :y (:y strike)
                 :x1 (:x1 strike)
                 :y1 (:y1 strike)
                 :thickness (:thickness strike)))
    (.close writer)))


(defn -main [& args]
  (let [{:keys [options arguments errors summary]}
        (parse-opts args cli-options)]
    (strike-out options)))
