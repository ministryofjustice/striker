(ns striker.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [clojure.java.io :as io])
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

(defn strike-out [& {:keys [input output page coords]}]
  "supply input & output PDFs, a page number & coordinates where the
  line should be drawn"
  (let [reader (make-reader input)
        writer (make-writer reader output)
        content (get-page writer page)]
    (do
      (draw-line :content content
                 :x (:x coords)
                 :y (:y coords)
                 :x1 (:x1 coords)
                 :y1 (:y1 coords)
                 :thickness (:thickness coords))
      (.close writer))))

(def cli-options
  [["-h" "--help"]
   ["-i" "--input INTPUT-PDF" "Template PDF file to start with"
    :default ""
    :parse-fn #(str (read-string %))]
   ["-o" "--output OUTPUT-PDF" "Name under which the new PDF file will be saved"
    :default ""
    :parse-fn #(str (read-string %))]
   ["-p" "--page page-num" "provide page number"
    :default ""
    :parse-fn #(read-string %)]
   [nil "--x x-coord" "x coordinate"
    :default ""
    :parse-fn #(read-string %)]
   [nil "--y y-coord" "y coordinate"
    :default ""
    :parse-fn #(read-string %)]
   [nil "--x1 x1-coord" "x1 coordinate"
    :default ""
    :parse-fn #(read-string %)]
   [nil "--y1 y1-coord" "y1 coordinate"
    :default ""
    :parse-fn #(read-string %)]
   ["-t" "--thickness num" "line thickness"
    :default ""
    :parse-fn #(read-string %)]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]}
        (parse-opts args cli-options)]
    (do
      (println options)
      (strike-out :input (:input options)
                  :output (:output options)
                  :page (:page options)
                  :coords {:x (:x options)
                           :y  (:y options)
                           :x1 (:x1 options)
                           :y1 (:y1 options)
                           :thickness (:thickness options)}))))
