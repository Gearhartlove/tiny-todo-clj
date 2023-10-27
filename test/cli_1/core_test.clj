(ns cli-1.core-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [cli-1.core :refer :all]))

; used to reset files for when resetting tests
(defn clear [file] (io/delete-file file true))
(defn test-file [] "/Users/gearhart/projects/clojure/cli/resources/todo-test.toml")
(defn test-data [] {:todo-list {:todo ["foo" "fuz"] :completed ["bar"]}})

(deftest clear-test
  (testing "Attempting to clear a file")
  (is (= true (clear "/Users/gearhart/projects/clojure/cli/resources/debug_todo.toml" ))))

(deftest save-test
  (test "Attempting to save data in the correct format")
  (clear (test-file))
  (save (test-data) (test-file))
  (is (=
        "\n[todo-list]\ntodo = [\"foo\", \"fuz\"]\ncompleted = [\"bar\"]\n"
        (slurp (test-file)))))

(deftest parse-test
  (test "Able to read from file and turn into map of toml values")
  (clear (test-file))
  (save (test-data) (test-file))
  (is (=
        {:todo-list {:todo ["foo" "fuz"] :completed ["bar"]}}
        (parse-toml-file (test-file)))))

(deftest get-chain
  (clear (test-file))
  (save (test-data) (test-file))
  (is (=
        ["foo" "fuz"]
        (get-in (parse-toml-file (test-file)) [:todo-list :todo]))))


