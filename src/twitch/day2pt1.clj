(ns twitch.day2pt1
  (:require [clojure.string :as str]))


(def input-file "resources/input2")

(slurp input-file)

(def selection->score {:rock     1
                       :paper    2
                       :scissors 3})

(def outcome->score {:lost 0
                     :draw 3
                     :won  6})

(def code->selection {"A" :rock
                      "B" :paper
                      "C" :scissors
                      "X" :rock
                      "Y" :paper
                      "Z" :scissors})

(def selection->outcome {[:rock :rock]     :draw
                         [:rock :paper]    :won
                         [:rock :scissors] :lost

                         [:paper :rock]     :lost
                         [:paper :paper]    :draw
                         [:paper :scissors] :won

                         [:scissors :rock]     :won
                         [:scissors :paper]    :lost
                         [:scissors :scissors] :draw})

(defn round->score [round]
  (let [codes           (str/split round #" ")
        selections      (map code->selection codes)
        round-score     (-> selections
                            selection->outcome
                            outcome->score)
        selection-score (-> codes
                            second
                            code->selection
                            selection->score)]
    (+ round-score selection-score)))

(defn read-strategy-guide [input-file]
  (->> input-file
       slurp
       str/split-lines
       (map round->score)
       (reduce +)
       ))

(read-strategy-guide input-file)
;; => 14827
