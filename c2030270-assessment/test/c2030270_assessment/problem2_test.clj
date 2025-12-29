(ns c2030270-assessment.problem2-test
  (:require [clojure.test :refer :all]
            [c2030270-assessment.problem2 :refer :all]))

(deftest get-student-plants-test
  (let [garden ["VRCGVVRVCGGCCGVRGCVCGCGV" "VRCCCGCRRGVCGCRVVCVGCGCV"]
        students ["Alice" "Bob" "Charlie" "David" "Eve" "Fred" "Ginny" "Harriet" "Ileana" "Joseph" "Kincaid" "Larry"]
        unordered-students ["Larry" "Alice" "Ileana" "Fred" "Charlie" "Bob" "David" "Eve" "Larry" "Kincaid" "Ginny" "Joseph"]]

    (testing "Returns correct plants for Alice"
      (is (= (get-student-plants garden students "Alice") '("violets" "radishes" "violets" "radishes"))))

    (testing "Returns correct plants for Bob"
      (is (= (get-student-plants garden students "Bob") '("clover" "grass" "clover" "clover"))))

    (testing "Returns correct plants with unordered students list"
      (is (= (get-student-plants garden unordered-students "Alice") '("violets" "radishes" "violets" "radishes"))))))

(deftest map-character-to-plant-test
  (testing "Maps garden letters to correct plant names"
    (is (= (map map-character-to-plant [\V \R \G \C]) '("violets" "radishes" "grass" "clover")))))

(deftest generate-garden-test
  (testing "Generates a garden with correct length of row for given amount of students"
    (let [amount-of-students 5 garden (generate-garden amount-of-students)]
      (is (= 2 (count garden)))
      (is (= (* 2 amount-of-students) (count (first garden))))
      (is (= (* 2 amount-of-students) (count (second garden)))))))