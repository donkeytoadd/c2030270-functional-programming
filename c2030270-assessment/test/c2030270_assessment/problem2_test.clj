(ns c2030270-assessment.problem2-test
  (:require [clojure.test :refer :all]
            [c2030270-assessment.problem2 :refer :all]
            [clojure.spec.alpha :as s]))

(def example-row1 "VRCGVVRVCGGCCGVRGCVCGCGV")
(def example-row2 "VRCCCGCRRGVCGCRVVCVGCGCV")

(def example-students
  ["Alice" "Bob" "Charlie" "David" "Eve" "Fred"
   "Ginny" "Harriet" "Ileana" "Joseph" "Kincaid" "Larry"])

(def unordered-students
  ["Larry" "Alice" "Ileana" "Fred" "Charlie" "Bob"
   "David" "Eve" "Larry" "Kincaid" "Ginny" "Joseph"])

(deftest get-student-plants-test
  (let [garden (->garden example-row1 example-row2)]

    (testing "Returns correct plants for Alice"
      (is (= (get-student-plants garden example-students "Alice") ["violets" "radishes" "violets" "radishes"])))

    (testing "Returns correct plants for Bob"
      (is (= (get-student-plants garden example-students "Bob") ["clover" "grass" "clover" "clover"])))

    (testing "Returns correct plants with unordered students list"
      ;get-student-plants expects a sorted list as the input, this is done in the normalise-students function in the solution
      (let [sorted-students (sort unordered-students)]
        (is (= (get-student-plants garden sorted-students "Alice") ["violets" "radishes" "violets" "radishes"]))))))

(deftest map-character-to-plant-test
  (testing "Maps garden letters to correct plant names"
    (is (= (mapv character->plant  [\V \R \G \C]) ["violets" "radishes" "grass" "clover"]))))

(deftest generate-garden-test
  (testing "Generates a garden with correct length of row for given amount of students"
    (let [amount-of-students 5 garden (generate-garden amount-of-students)]
      (is (s/valid? :c2030270-assessment.problem2/garden-record garden))
      (is (= (* 2 amount-of-students) (count (:row1 garden))))
      (is (= (* 2 amount-of-students) (count (:row2 garden)))))))

(deftest garden-constructor-test
  (testing "Valid garden record"
    (is (s/valid? :c2030270-assessment.problem2/garden-record (->garden example-row1 example-row2))))

  (testing "Incorrect garden row length throws exception"
    (is (thrown? clojure.lang.ExceptionInfo (->garden "VR" "VRC"))))

  (testing "Invalid characters for plants throws exception"
    (is (thrown? clojure.lang.ExceptionInfo (->garden "XY" "VR")))))

(deftest get-student-index-test
  (testing "Returns correct index for inputted name"
    (is (= 0 (get-student-index default-student-list "Alice"))))

  (testing "Throws exception when name not found"
    (is (thrown? clojure.lang.ExceptionInfo (get-student-index default-student-list "Bartholomew")))))