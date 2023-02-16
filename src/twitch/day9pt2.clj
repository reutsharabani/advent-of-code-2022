(ns twitch.day9pt2
  (:require [clojure.string :as str]))

(def input-file "resources/input9")

(defn command->seq [command-str]
  (let [[direction repeats-str] (str/split command-str #" ")
        repeats (Integer/parseInt repeats-str)]
    (->> direction
         (repeat repeats)
         (apply str))))

;; (command-str->seq "U 4")

(def rope-length 10)

(def start-state {:rope (repeat rope-length [0 0])
                  :unique-tail-positions #{}})

(def command->delta
  {\U [0, 1]
   \D [0, -1]
   \L [-1, 0]
   \R [1, 0]})

(def temp?-delta->consolidation-delta
  {
   ;; UR
   [0 2] [0 1]
   ;; UR
   [1 2] [1 1]
   [2 1] [1 1]
   ;; R
   [2 0] [1 0]
   ;; RD
   [2 -1] [1 -1]
   [1 -2] [1 -1]
   ;; D
   [0 -2] [0 -1]
   ;; DL
   [-1 -2] [-1 -1]
   [-2 -1] [-1 -1]
   ;; L
   [-2 0] [-1 0]
   ;; LU
   [-2 1] [-1 1]
   [-1 2] [-1 1]
   [2 2] [1 1]
   [-2 2] [-1 1]
   [2 -2] [1 -1]
   [-2 -2] [-1 -1]
   })

(defn apply-command [state command]
  (loop [rope-processed []
         rope-left (:rope state)]
    (if-let [[current & new-rope-left] rope-left]
      (let [previous (last rope-processed)
            delta (if previous
                    (-> (map - previous current)
                        (temp?-delta->consolidation-delta)
                        (or [0 0]))
                    (command->delta command))
            new-current (map + current delta)]
        (recur (conj rope-processed new-current)
               new-rope-left))
      {:rope rope-processed
       :unique-tail-positions (-> state
                                  :unique-tail-positions
                                  (conj (last rope-processed)))})))

;; (reduce apply-command start-state "ULULULUL")

(defn tail-positions [input-file]
  (let [commands (-> input-file
                     slurp
                     str/split-lines)
        commands-seq (->> commands
                          (map command->seq)
                          (apply str))
        final-state (reduce apply-command start-state commands-seq)]
    (count (:unique-tail-positions final-state))))

(tail-positions input-file)
;; => 2303
