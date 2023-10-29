(ns cli-1.core
  (:gen-class)
  (:require [clojure.test :refer :all]
            [toml.core :as toml]))

(defn todo-file [] "/Users/gearhart/projects/clojure/cli/resources/todo.toml")

(defn parse-user-input
  "match on command and return the desired command and it's arguments"
  [& args]
  (let
    [maybe-command (.toLowerCase (str (first (flatten args))))
     maybe-args (second (flatten args))
     cmd (condp = maybe-command
               "add"      :add
               "list"    :list
               "remove"  :remove
               "done"    :complete
               :unknown)]
    [cmd maybe-args]
    ))

(defn validate-user-input [[cmd args]]
  (when (= :unknown cmd)
    (throw
      (ex-info "Unknown command" {:cause :unknown-command})))
  (when
    (and
      (= :add cmd)
      (= nil  args))
    (throw
      (ex-info "Missing second argument" {:cause :missing-second-arg})))
  [cmd args])

(defn save
  "Saves the todo list data to the todo file."
  ([data] (spit (todo-file) (toml/write data)))
  ([data file] (spit file (toml/write data))))

(defn parse-toml-file
  "parses specified file to a keywordize-d map"
  ([file] (toml/read (slurp file) :keywordize)))

(defn existing-data [] (parse-toml-file (todo-file)))

(defn listing
  ([key] (println (get-in (existing-data) key)))
  ([title key] (println title (get-in (existing-data) key))))

(defn remove-from [key arg data]
  ; returns the data source the data was removed from
  (assoc-in
     data
     key
     (remove #{arg} (get-in data key))))

(defn add-to [key arg data]
  (assoc-in
    data
    key
    (conj (get-in data key) arg)))

(defn execute
  "Mutate todo data. Can change the :todo OR :completed keywords. Returns the mutated data."
  [[cmd arg]]
  (case cmd
    :add (do
           (save (add-to [:todo-list :todo] arg (existing-data)))
           (listing "TODO" [:todo-list :todo]))
    :list (do
            (listing "TODO" [:todo-list :todo])
            (listing "COMPLETED" [:todo-list :completed]))
    :remove (do
              (save (remove-from [:todo-list :todo] arg (existing-data)))
              (listing "TODO" [:todo-list :todo]))
    :complete (do
                (save
                  (add-to
                  [:todo-list :completed]
                  arg
                  (remove-from [:todo-list :todo] arg (existing-data))))
                (listing "COMPLETED" [:todo-list :completed]))))


(defn -main
  [& args]
  (->>
    (parse-user-input args)
    (validate-user-input)
    (execute)))