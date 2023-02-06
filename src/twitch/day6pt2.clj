(ns twitch.day6pt2
  (:require [clojure.string :as str]))

(def input-file "resources/input6")

(def message-sequence-length 14)

(defn start? [chars]
  (= message-sequence-length (count (set chars))))

(defn find-start [input-file]
  (let [signals (slurp input-file)]
    (->> signals
         (partition message-sequence-length 1)
         (take-while (complement start?))
         count
         (+ message-sequence-length))))

(find-start input-file)
;; => 3774
