(ns twitch.day9pt1
  (:require [clojure.string :as str]))

(def input-file "resources/input9")

(defn command->seq [command-str]
  (let [[direction repeats-str] (str/split command-str #" ")
        repeats (Integer/parseInt repeats-str)]
    (->> direction
         (repeat repeats)
         (apply str))))

;; (command-str->seq "U 4")

(def start-state {:head [0 0]
                  :tail [0 0]
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
   [-1 2] [-1 1]})

(defn apply-command [state command]
  (let [head (:head state)
        tail (:tail state)
        delta (command->delta command)
        new-head (map + head delta)
        temp?-delta (map - new-head tail)
        consolidation-delta (get temp?-delta->consolidation-delta temp?-delta [0, 0])
        new-tail (map + tail consolidation-delta)]
    {:head new-head
     :tail new-tail
     :unique-tail-positions (conj (:unique-tail-positions state) new-tail)}))

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
;; => 5907
