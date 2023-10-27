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
               "delete"  :remove
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

(defn list-todo []
  (prn (get-in (existing-data) [:todo-list :todo])))

(defn execute
  "Mutate todo data. Can change the :todo OR :completed keywords. Returns the mutated data."
  [[cmd arg]]
  (case cmd
    :add (do
           (save
             (assoc-in
               (existing-data)
               [:todo-list :todo]
               (conj (get-in (existing-data) [:todo-list :todo]) arg)))
           (list-todo))
    :list (list-todo)
    :remove (save (remove #(= % arg) (existing-data)))
    :complete (save
                ((remove #(= % arg) (existing-data))
                 (conj (get-in (existing-data) [:todo-list :completed]) arg)))))


(defn -main
  [& args]
  (->>
    (parse-user-input args)
    (validate-user-input)
    (execute)))

(comment
  ":GOALS
  * upload cli executable to github
  * learn some clojure!

  :TODO-LIST
  (short-list)
  * clean interface
  * add task
  * list task
  * mark task as completed
  * delete task
  * save and load task
  * re-order-able tasks
  (long-list)
  * Add Task: Allow users to add tasks to the to-do list.
  Example command: add Buy groceries
  * List Tasks: Display the list of tasks.
  Example command: list
  * Mark Task as Completed: Allow users to mark a task as completed.
  Example command: done 1 (marks the task with index 1 as completed)
  * Delete Task: Allow users to delete a task from the list.
  Example command: delete 2 (deletes the task with index 2)
  * Save and Load Tasks: Implement functionality to save the tasks to a file and load them when the program starts. This will allow users to persist their to-do list across sessions.")