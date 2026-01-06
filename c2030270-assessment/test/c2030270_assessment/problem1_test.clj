(ns c2030270-assessment.problem1-test
  (:require [clojure.test :refer :all]
            [clojure.spec.test.alpha :as st :refer :all]
            [c2030270-assessment.problem1 :refer [square-each-value-in-list]]))


(deftest square-list-test
  (testing "Valid list containing integers"
    (is (= (square-each-value-in-list [1 2 3 4]) `(1 4 9 16)))
    (is (= (square-each-value-in-list [0]) `(0)))
    (is (= (square-each-value-in-list [-1 -2 -3]) `(1 4 9))))

  (testing "Different numeric types within the list"
    (is (= (square-each-value-in-list [1.5 2.5 3.5]) `(2.25 6.25 12.25)))
    (is (= (square-each-value-in-list [1/2 1/4 1/5]) `(1/4 1/16 1/25)))
    (is (= (square-each-value-in-list [1N 2N 3N]) `(1N 4N 9N))))

  (testing "Mixed numeric types within the list"
    (is (= (square-each-value-in-list [1 2N 3.5]) `(1 4N 12.25)))
    (is (= (square-each-value-in-list [1.0 2/3 10M]) `(1.0 4/9 100M))))

  (testing "Non-numeric data within the list"
    (is (empty? (square-each-value-in-list ["this" "is" "a" "test"])))
    (is (empty? (square-each-value-in-list ["hello" :world]))))

  (testing "Mixed list of numeric and non-numeric data"
    (is (= (square-each-value-in-list [1 "hello" 3 :world 5]) `(1 9 25)))
    (is (= (square-each-value-in-list [1 nil 3]) `(1 9))))

  (testing "Edge cases"
    (is (= (square-each-value-in-list []) `()))))

;runs 1000 random tests via st/check that follow the specs defined in the solution file.
(deftest square-list-spec-test
  (testing "Generative tests using specs "
    (is (true? (:result (:clojure.spec.test.check/ret (first (st/check `square-each-value-in-list))))))))