(ns twitch.day4pt2
  (:require [clojure.string :as str]))

(def input-file "resources/input4")

(defn assignment->range [assignment]
  (-> assignment
      (str/split #"-")
      (->> 
       (map read-string))))

;;    -------
;;  ----

;; -------
;;  ----

;; -------
;;      ----

(defn ranges-overlap? [[sr1 er1] [sr2 er2]]
  (or (and (<= sr1 er2)
           (>= er1 sr2))
      (and (<= sr2 er1)
           (>= er2 sr1))))

(defn assignment-overlaps? [assignment]
  (let [[fr sr] (map assignment->range (str/split assignment #","))]
    (ranges-overlap? fr sr)))

(defn find-full-overlaps [input-file]
  (->> input-file
       slurp
       str/split-lines
       (filter assignment-overlaps?)
       count))

(find-full-overlaps input-file)
;; => 804
