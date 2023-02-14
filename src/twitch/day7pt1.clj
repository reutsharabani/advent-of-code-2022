(ns twitch.day7pt1
  (:require [clojure.string :as str]))

(def input-file "resources/input7")

(defn file? [entry]
  (str/starts-with? entry ""))

(defn directory? [entry]
  (str/starts-with? entry "dir "))

(defn directory-name [entry]
  (-> entry
      (str/split #" ")
      (nth 2)))

(defn dfs-scan->tree [dfs-scan & {:keys [path]
                                  :or {path []}}]
  (let [entry (first dfs-scan)
        _rest (rest dfs-scan)]
    (cond
      (nil? entry) {}
      ;; go back one dir (exit node)
      (= "$ cd .." entry) (dfs-scan->tree _rest :path (into [] (butlast path)))
      ;; skip ls and dir
      (= "$ ls" entry) (dfs-scan->tree _rest :path path)
      (str/starts-with? entry "dir") (dfs-scan->tree _rest :path path)
      ;; cd into dir
      (str/starts-with? entry "$ cd ") (let [directory-name (directory-name entry)]
                                         (dfs-scan->tree _rest :path (conj path directory-name)))
      ;; file
      :default (let [[file-size-string file-name] (str/split entry #" ")
                     new-path (conj path file-name)
                     tree (dfs-scan->tree _rest :path path)
                     file-size (read-string file-size-string)]
                 (assoc-in tree new-path file-size)))))

;; cache ?
(defn size [tree-or-size]
  (if (map? tree-or-size)
    (apply + (map size (vals tree-or-size)))
    tree-or-size))

(defn sum-directories [tree max-size]
  (reduce + (for [[k v] tree]
              (if (map? v)
                (if (<= (size v) max-size)
                  (+ (size v) (sum-directories v max-size))
                  (sum-directories v max-size))
                0))))

(defn count-directories-of-size [input-file & {:keys [max-size]
                                               :or {max-size 100000}}]
  (-> input-file
      slurp
      str/split-lines
      dfs-scan->tree
      (sum-directories 100000)))


(count-directories-of-size input-file :max-size 100000)
;; => 1783610
