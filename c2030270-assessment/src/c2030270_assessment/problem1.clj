(ns c2030270-assessment.problem1
  (:require [clojure.spec.alpha :as s]))

(s/def ::input-collection (s/coll-of any?))
(s/def ::output-collection (s/coll-of number?))

(defn square-each-value-in-list [list]
  (map #(* % %) (filter number? list)))

;function defintion that will be used within the unit tests. allows automated testing using spec.test.aplha.check
(s/fdef square-each-value-in-list
  ;defines valid inputs for the function
  :args (s/cat :collection ::input-collection)
  ;defines what the function return should look like
  :ret  ::output-collection)

(defn main []
  (let [input [2 4.5 1/4 "apple" nil :cat 10]]
    (println "\nInput List: " input)
    (println "Result: " (vec (square-each-value-in-list input)))))