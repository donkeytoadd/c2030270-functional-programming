(ns c2030270-assessment.problem2
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(def default-student-list ["Alice" "Bob" "Charlie" "David" "Eve" "Fred" "Ginny" "Harriet" "Ileana" "Joseph" "Kincaid" "Larry"])
(def default-row1 "VRCGVVRVCGGCCGVRGCVCGCGV")
(def default-row2 "VRCCCGCRRGVCGCRVVCVGCGCV")

(def plant-codes #{\G \C \R \V})
(def character->plant {\G "grass" \C "clover" \R "radishes" \V "violets"})

;valid student name consists of a non blank string
(s/def ::student-name (s/and string? #(not (str/blank? %))))

;a garden row must be a string that is made up of valid plant codes
(s/def ::row (s/and string? (partial every? plant-codes)))

(defrecord Garden [row1 row2])

(s/def ::garden-record
  (s/and
   #(instance? Garden %)
   ;checks the rows of the garden against the row specification to validate them
   ;also checks if rows are of equal length
   (fn [garden]
     (and (s/valid? ::row (:row1 garden))
          (s/valid? ::row (:row2 garden))
          (= (count (:row1 garden))
             (count (:row2 garden)))))))

;a wrapper for the defrecord Garden constructor above. adds validation by checking against the garden specifciation
(defn ->garden [row1 row2]
  (let [garden (->Garden row1 row2)]
    (when-not (s/valid? ::garden-record garden)
      (throw (ex-info "Invalid garden rows" {:row1 row1 :row2 row2})))
    garden))

(defn generate-garden [amount-of-students]
  (let [random-plant #(rand-nth (vec plant-codes))
        row-length (* 2 amount-of-students)
        create-row #(apply str (repeatedly row-length random-plant))]
    (->garden (create-row) (create-row))))

(defn generate-students []
  (let [amount-of-students (loop []
                             (println "Enter the amount of students you want in your list")
                             (flush)
                             (let [input (read-line)]
                               (if (re-matches #"\d+" input)
                                 (let [n (Integer/parseInt input)]
                                   (if (pos? n)
                                     n
                                     (do (println "Student list must be at least 1") (recur))))
                                 (do (println "Invalid input, please enter a whole number") (recur)))))]
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

(defn normalise-students [students]
  ;removes any duplicate names and sorts alphabetically, then ensures a vector is returned
  (->> students distinct sort vec))

(defn get-student-index [sorted-students student]
  (let [index (.indexOf sorted-students student)]
    (when (= -1 index)
      (throw (ex-info "Student not found" {:student student})))
    index))

(defn get-student-plants [garden students student]
  (let [index (get-student-index students student)
        start (* index 2)
        end   (+ start 2)
        row1  (:row1 garden)
        row2  (:row2 garden)
        cups  (str (subs row1 start end) (subs row2 start end))]
    (mapv character->plant cups)))

(defn main []
  (println "Choose a mode:\n1: Default(student list & garden same as brief)\n2: Interactive (make your own student list & random garden generation)")
  (flush)
  (let [choice (str/trim (read-line))
        use-default (= "1" choice)
        students (if use-default default-student-list (normalise-students (generate-students)))
        garden (if use-default (->garden default-row1 default-row2) (generate-garden (count students)))]

    (println "\nStudents:")
    (println students)

    (println "\nGarden:")
    (println (:row1 garden))
    (println (:row2 garden))

    (println "\n Type 'exit' to quit")

    ;loops until user exits, checks inputted name and returns error if not found in list
    (loop []
      (println "\nEnter a student's name to see their plants:")
      (flush)
      (let [input (read-line)]
        (when-not (= "exit" input)
          (try
            (println input "has plants:" (get-student-plants garden students input))
            (catch Exception ex
              (println "\nError:" (.getMessage ex))))
          (recur))))))

(main)