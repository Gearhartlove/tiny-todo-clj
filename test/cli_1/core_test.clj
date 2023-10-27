(ns cli-1.core-test
  (:require [clojure.test :refer :all]
            [cli-1.core :refer :all]))

(deftest clear-test
  (testing "Attempting to clear a file")
  (is (= true (clear "/Users/gearhart/projects/clojure/cli/resources/debug_todo.txt" ))))

(deftest prepare-data-test
  (test "Preparing the data to look like :todo[]/n:complete[] in csv for consistent parsing")
  (let [data {:todo ["Go to the store" "Rock climb" "Take regina to the harry potter candy store"], :complete '("program for work" "try harder")}
        writeToFile (str ":todo " (get data :todo) "\n" ":complete " (get data :complete))]
    (is (=
          writeToFile
          ":todo [\"Go to the store\" \"Rock climb\" \"Take regina to the harry potter candy store\"]\n:complete (\"program for work\" \"try harder\")"))))

(deftest save-test
  (test "Attempting to save data in the correct format")
  (let [test-file "/Users/gearhart/projects/clojure/cli/resources/debug_todo.txt"
        data {:todo ["Go to the store" "Rock climb" "Take regina to the harry potter candy store"], :complete '("program for work" "try harder")} ]
    (clear test-file)
    (save data test-file)
    (is (=
          ":todo [\"Go to the store\" \"Rock climb\" \"Take regina to the harry potter candy store\"]\n:complete (\"program for work\" \"try harder\")"
          (slurp test-file)))))
