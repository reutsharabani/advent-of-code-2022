(ns twitch.day1pt1
  (:require [clojure.string :as str]))


(def input-file "resources/input")

(defn file->batches [input-file]
  (let [content (slurp input-file)]
    (str/split content #"\n\n")))

(defn batch->sum [batch]
  (->> batch
       str/split-lines
       (map read-string)
       (apply +)))


(->> input-file
     file->batches
     (map batch->sum)
     (apply max))
;; => 67633
