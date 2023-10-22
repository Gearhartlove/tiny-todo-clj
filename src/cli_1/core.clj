(ns cli-1.core)

(defn parse
  "match on command and return the desired command and it's arguments"
  [& args]
  (let
    [maybe-command (.toLowerCase (str (first (first args))))
     cmd (condp = maybe-command
               "add"     :add
               "list"    :list
               "delete"  :remove
               "done"    :complete
               :unknown)]
    cmd
    ))

(defn validate [cmd]
  (println cmd))

(defn -main
  [& args]
  (->>
    (doto (parse args) prn)
    (validate)))

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
