(ns c2030270-assessment.problem2
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(s/def ::student-name (s/and string? #(not (str/blank? %))))
(s/def ::students (s/coll-of ::student-name :kind vector? :min-count 1))

(defn generate-students []
  (println "Input the number of students you want: ")
  (flush)
  (let [amount-of-students (try (Integer/parseInt (read-line))
                                (catch Exception ex
                                  (println "Error:" (.getMessage ex))
                                  (generate-students)))]
    (loop [count 0 students []]
      (if (= count amount-of-students)
        students
        (do
          (println (str "Enter name for student " (inc count) ": "))
          (flush)
          (let [name (read-line)]
            (if (s/valid? ::student-name name)
              (recur (inc count) (conj students name))
              (do (println "Invalid name, please try again") (recur count students)))))))))

;load from config file or database type structure, makes it better
(def plant-codes [\G \C \R \V])

(defn generate-garden [amount-of-students]
  (let [random-plant #(rand-nth plant-codes)
        row-length (* 2 amount-of-students)
        create-row #(apply str (repeatedly row-length random-plant))]
    [(create-row) (create-row)]))

(def map-character-to-plant {\G "grass" \C "clover" \R "radishes" \V "violets"})

(defn get-student-plants [garden students student]
  (let [[row1 row2] garden
        sorted-students (sort students)
        index (.indexOf sorted-students student)
        start (* index 2)
        end (+ start 2)
        student-cups (concat (subs row1 start end) (subs row2 start end))]
    (map map-character-to-plant student-cups)))

(defn main []
  (let [students (generate-students)
        garden (generate-garden (count students))
        [row1 row2] garden]
    (println "\nStudents: " students)
    (println "\nGarden:\n" row1 "\n" row2)
    (println "\nEnter a student's name to see their plants:")
    (let [input (read-line)]
      (println input "has plants:" (vec (get-student-plants garden students input))))))

(main)