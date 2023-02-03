(ns twitch.day5pt1
  (:require [clojure.string :as str]))

(def input-file "resources/input5")


(defn zip [& colls]
  (partition (count colls) (apply interleave colls)))

(defn parse-stack [stack-input]
  (let [[_ number _] (last stack-input)]
    number))

(defn char->int [c]
  (- (int c) (int \0)))

(defn parse-stacks [input]
  ;; parse into {stack#->[crates]}
  (->> input
       str/split-lines
       (map (partial drop 1))
       (map butlast)
       (map (partial partition 1 4))
       (apply zip)
       (map reverse)
       (map (partial apply concat))
       (map (partial apply str))
       (map str/trim)
       (map (partial (juxt (comp char->int first) rest)))
       (into {})
       ))


(defn parse-move [move-input]
  ;; "move 5 from 4 to 5" -> [5 4 5]
  (-> move-input
      (str/split #" ")
      (->>
       (drop 1)
       (partition 1 2)
       (into [] cat)
       (map read-string))))

(defn apply-move [stacks move]
  (let [[how-many source-stack target-stack] move
        crates-in-stack (get stacks source-stack)
        crates-left     (->> crates-in-stack
                            reverse
                            (drop how-many)
                            reverse)
        crates-moved (-> crates-in-stack
                           reverse
                           (->>
                           (take how-many)))]
    (-> stacks
        (update target-stack concat crates-moved)
        (assoc source-stack crates-left ))))

(apply-move {1 [\a \b \c]
             2 [\d \e \f]}
            [2 2 1])

(let [[stacks-input moves-input] (-> input-file
                                     slurp
                                     (str/split #"\n\n"))
      moves (map parse-move (str/split-lines moves-input))
      stacks-start (parse-stacks stacks-input)
      stacks-final (reduce apply-move stacks-start moves)]

  (comment (for [stack (sort (keys stacks-final))]
             (get stacks-final stack)))
  (->> stacks-final
      sort
      (map second)
      (map last)
      (apply str)))
;; => "RLFNRTNFB"
