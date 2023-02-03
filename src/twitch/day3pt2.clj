(ns twitch.day3pt2
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input-file "resources/input3")

(slurp input-file)

(int \a)
(int \A)
(def abc "abcdefghijklmnopqrstuvwxyz")
(def ABC "ABCDEFGHIJKLMNOPQRSTUVWXYZ")

(def letters->priority (into {} cat
                             [;; lower
                              (let [base-val (dec (int \a))]
                                (for [letter abc]
                                  [(-> letter str keyword) (- (int letter) base-val)]))
                              ;; upper
                              (let [base-val (- (dec (int \A)) 26)]
                                (for [letter ABC]
                                  [(-> letter str keyword) (- (int letter) base-val)]))
                              ]))

(defn find-group-shared-items [rucksacks]
  (->> rucksacks
      (map set)
      (apply set/intersection)))

(defn shared-item-priorities [input-file]
  (->> input-file
      slurp
      str/split-lines
      (partition 3)
      (map find-group-shared-items)
      (into [] cat)
      (map str)
      (map keyword)
      (map letters->priority)
      (reduce +)))


(shared-item-priorities input-file)
;; => 2616
