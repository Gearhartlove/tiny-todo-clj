(ns cli-1.core
  (:require [clojure.test :refer :all]))

(defn parse
  "match on command and return the desired command and it's arguments"
  [& args]
  (let
    [maybe-command (.toLowerCase (str (first (first args))))
     maybe-args (str (second (first args)))
     cmd-args (condp = maybe-command
               "add"      [:add maybe-args]
               "list"    :list
               "delete"  :remove
               "done"    :complete
               :unknown)]
    cmd-args
    ))

(defn validate [cmd-args]
  (when (= :unknown cmd-args)
    (throw
      (ex-info "Unknown command" {:cause :unknown-command})))
  (when
    (and
      (= :add (first cmd-args))
      (= nil  (second cmd-args)))
    (throw
      (ex-info "Missing second argument" {:cause :missing-second-arg})))
  cmd-args)

(defn read-in-file []
  (slurp "../../resources/todo.txt"))

(defn parse-file [src]
  (->> src
       ; note, best to start with split-lines
       ; because the regex yells at you otherwise
       (clojure.string/split-lines)
       (map #(clojure.string/split % #":"))
       (map (fn [[k v]] [(keyword k) (clojure.string/split v #",")]))
       (into {})))

; KGF : TODO TEST
(defn get-data []
  (parse-file (read-in-file)))

(prn (get-data))


; KGF : need "object" to add items to
; note : it's called a todo LIST
;        maybe I can create this using lists!
; > it would be nice if I could reorder items. Vec
;   would be better for this.
; > I will make completed a list so that it's easy to
;   add to and undo
; schema :
; * todo ["Go to the store","program clojure", "eat dinner with family"]
; * complete ("eat breakfast", "bake macaroni")
(defn execute [cmd-args]
  (let [
        cmd (first cmd-args)
        arg? (second cmd-args)
        ; read in file
        todo '()
        ]
    (case cmd
      ; kgf : use 'get-data' function to get saved map of todo list, add the args to the map. write the map out afterwords
      :add (spit "todo.txt"
                 (conj todo arg?))
      :list todo
      :remove ()
      :complete ())
    ))

;(prn "adding")
;(prn (execute [:add "Go to the store"]))
;(prn "listing")
;(prn (execute [:list]))

;(defn -main
;  [& args]
;  (->>
;    (parse args)
;    (validate)
;    (execute)))

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

; KGF : Keeping for learning on let functions
;(defn parse-todo [src]
;  (let [parse (comp
;                (let [key #(keyword (first %))
;                      val #(second %)]
;                  (fn [pair] [(key pair) (val pair)]))
;                #(clojure.string/split % #":")
;                #(first %)
;                #(clojure.string/split % #"\n"))]
;    (parse src)))



(clojure.test/is
  (=
    {:todo ["Go to the store" "Rock climb"]}
    (parse-file "todo:Go to the store,Rock climb")))
(clojure.test/is
  (=
    {:complete ["program for work" "try harder"]}
    (parse-file "complete:program for work,try harder")))

(clojure.test/is
  (=
    {:todo ["Go to the store" "Rock climb"] :complete ["program for work" "try harder"]}
    (parse-file "todo:Go to the store,Rock climb\ncomplete:program for work,try harder")))

