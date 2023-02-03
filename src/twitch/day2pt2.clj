(ns twitch.day2pt2
  (:require [clojure.string :as str]))


(def input-file "resources/input2")

(slurp input-file)

(def selection->score {:rock     1
                       :paper    2
                       :scissors 3})

(def outcome->score {:lose 0
                     :draw 3
                     :win  6})

(def selection-code->selection {"A" :rock
                                "B" :paper
                                "C" :scissors})

(def outcome-code->outcome {"X" :lose
                            "Y" :draw
                            "Z" :win})

(def outcome->selection->my-selection {:win  {:rock     :paper
                                              :paper    :scissors
                                              :scissors :rock}
                                       :draw {:rock :rock
                                              :paper :paper
                                              :scissors :scissors}
                                       :lose {:rock     :scissors
                                              :paper    :rock
                                              :scissors :paper}})

(defn round->score [round]
  (let [[selection-code outcome-code] (str/split round #" ")
        selection                     (selection-code->selection selection-code)
        outcome                       (outcome-code->outcome outcome-code)
        round-score                   (outcome->score outcome)
        my-selection                  (get-in outcome->selection->my-selection [outcome selection])
        selection-score               (selection->score my-selection)]
  (+ round-score selection-score)))

(defn read-strategy-guide [input-file]
  (->> input-file
       slurp
       str/split-lines
       (map round->score)
       (reduce +)
       ))

(read-strategy-guide input-file)
;; => 13889

;; :-)
