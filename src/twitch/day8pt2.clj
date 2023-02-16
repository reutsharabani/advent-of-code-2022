(ns twitch.day8pt2
  (:require [clojure.string :as str]))

(def input-file "resources/input8")

(defn parse-line [line]
  (map #(Integer/parseInt (str %)) line))

(defn get-neighbors [x y]
  [[(dec x) y]
   [x (dec y)]
   [(inc x) y]
   [x (inc y)]])

(defn any? [pred coll]
  (not (every? (complement pred) coll)))

(defn edge? [m x y]
  (let [edge-n (dec (count m))]
    (or (zero? x)
        (zero? y)
        (= edge-n x)
        (= edge-n y))))

(defn scenic-score
  ([m x y x-delta y-delta height]
   (let [current-height (get-in m [x y])]
     (if (or (edge? m x y)
             (>= current-height height))
       0
       (inc (scenic-score m (+ x x-delta) (+ y y-delta) x-delta y-delta height)))))
  ([m x y]
   (let [height (get-in m [x y])]
     (if (edge? m x y)
       0
       ;; we don't care about edge trees (zero multiplied by anything is zero anyway)
       (*
        (inc (scenic-score m x (inc y) 0 1 height))
        (inc (scenic-score m (inc x) y 1 0 height))
        (inc (scenic-score m x (dec y) 0 -1 height))
        (inc (scenic-score m (dec x) y -1 0 height)))))))

;; 1  2  3  4
;; 5  6  7  8
;; 9  10 11 12
;; 13 14 15 16
;; (def m [[1 2 3 4] [5 6 7 8] [9 10 11 12] [13 14 15 16]])
;; (def mi (->> input-file
;;              slurp
;;              str/split-lines
;;              (map parse-line)
;;              (vec->map)))
;;(scenic-score mi 38 9)

(defn vec->map [v]
  (let [xys (for [x (range (count v))
                  y (range (count (first v)))]
              [x y])]
    (reduce (fn [m [x y :as xy]]
              (assoc-in m xy (-> v
                                 (nth x)
                                 (nth y))))
            {}
            xys)))

;; (vec->map m)

(defn max-scenic-score [input-file]
  (let [m (->> input-file
               slurp
               str/split-lines
               (map parse-line)
               vec->map)
        n (count m)]
    (apply max (for [x (range n)
                     y (range n)]
                 (do
                   (when (= (scenic-score m x y) 595080)
                     (print x y))
                   (scenic-score m x y))))))

(max-scenic-score input-file)
;; => 595080

;; good night :)
