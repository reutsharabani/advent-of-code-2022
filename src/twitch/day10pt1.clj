(ns twitch.day10pt1
  (:require [clojure.string :as str]))


(def input-file "resources/input10")
;; (def input-file "resources/test_input10")

(def commands {:noop (fn [cycles _]
                       (let [[c x] (last cycles)]
                         (conj cycles [(inc c) x])))
               :addx (fn [cycles v]
                       (let [[c x] (last cycles)]
                         (conj cycles [(inc c) x] [(+ c 2) (+ x v)])))})

(defn parse-command [cmd]
  (let [[n v] (str/split cmd #" ")]
    (cond
      (= n "noop") [:noop]
      (= n "addx") [:addx (Integer/valueOf v)]
      )))

(defn run-command [cycles [n v]]
  (let [command (n commands)]
    (command cycles v)))

(defn process [inf indices]
  (-> inf
      slurp
      str/split-lines
      (->>
       (map parse-command)
       (reduce run-command [[1 1]])
       (into {}))
      (select-keys indices)
      (->> (reduce (fn [s [x v]]
                     (+ s (* x v))) 0))))

(comment
  (process input-file [20
                       60
                       100
                       140
                       180
                       220]))
