(ns twitch.day7pt2
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

(defn tree->dir-sizes [tree]
  (into [] cat
        (for [[k v] tree]
          (if (map? v)
            (conj (tree->dir-sizes v) (size v))
            []))))

(defn find-smallest-dir-to-delete [tree total needed]
  (let [used (size tree)
        target (- needed (- total used))]
    (->> tree
         tree->dir-sizes
         ;; only take big enough dirs
         (filter #(> % target))
         ;; take minimal dir
         (apply min))))

(defn count-directories-of-size [input-file & {:keys [max-size]
                                               :or {max-size 100000}}]
  (-> input-file
      slurp
      str/split-lines
      dfs-scan->tree
      (find-smallest-dir-to-delete 70000000 30000000)))


(count-directories-of-size input-file :max-size 100000)
;; => 4370655
