(ns twitch.day3pt1
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

(defn find-rucksack-shared-items [rucksack]
  (let [[first-compartment second-compartment] (-> rucksack
                                                   count
                                                   (/ 2)
                                                   (split-at rucksack)
                                                   (->>
                                                    (map set)))]
    (set/intersection first-compartment second-compartment)))

(defn shared-item-priorities [input-file]
  (->> input-file
      slurp
      str/split-lines
      (map find-rucksack-shared-items)
      (into [] cat)
      (map str)
      (map keyword)
      (map letters->priority)
      (reduce +)))


(shared-item-priorities input-file)
;; => 7848
