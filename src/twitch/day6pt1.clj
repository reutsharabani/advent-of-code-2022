(ns twitch.day6pt1)

(def input-file "resources/input6")

(def start-sequence-length 4)

(defn start? [chars]
  (= start-sequence-length (count (set chars))))

(defn find-start [input-file]
  (let [signals (slurp input-file)]
    (->> signals
        (partition start-sequence-length 1)
        (take-while (complement start?))
        count
        (+ start-sequence-length))))

(find-start input-file)
;; => 1623
