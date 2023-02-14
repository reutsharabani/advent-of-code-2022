(ns twitch.day8pt1
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

(defn is-visible?
  ([m x y x-delta y-delta height]
   (let [current-height (get-in m [x y])]
     (and (< current-height height)
          (or (edge? m x y)
              (is-visible? m (+ x x-delta) (+ y y-delta) x-delta y-delta height)))))
  ([m x y]
   (let [height (get-in m [x y])]
     (or
      ;; we reached an edge
      (edge? m x y)
      (is-visible? m x (inc y) 0 1 height)
      (is-visible? m (inc x) y 1 0 height)
      (is-visible? m x (dec y) 0 -1 height)
      (is-visible? m (dec x) y -1 0 height)))))

;; 163
;; 656
;; 789
;; (def m [[1 6 3] [4 5 6] [7 8 9]])
;; (is-visible? (vec->map m) 1 1)

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

(defn count-visible-trees [input-file]
  (let [m (->> input-file
               slurp
               str/split-lines
               (map parse-line)
               vec->map)
        n (count m)]
    (reduce + (for [x (range n)
                    y (range n)]
                (if (is-visible? m x y)
                  1
                  0)))))

(count-visible-trees input-file)
;; => 1543
