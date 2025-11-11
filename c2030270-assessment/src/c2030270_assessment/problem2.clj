(ns c2030270-assessment.problem2
  (:require [clojure.string :as str :refer [split]]))

(defn normalise-garden-input [garden]
  (cond
    (and
    ;checks garden input to see if it is some kind of ordered sequence (list, vector or lazy sequence) 
     (sequential? garden)
     ;checks if input is 2 strings
     (= 2 (count garden))
     (every? string? garden))
    (vec garden)

    ;if input is a string, checks if its split with either a space or newline character, and then splits the string on that character
    (and (string? garden) (re-find #"[ \n]" garden))
    (vec (str/split garden #"[ \n]"))

    ;if input is one long string, then it finds the midpoint and creates a vector madeup of 2 items, each being a substring of the original input string
    (string? garden)
    (let [garden-length (count garden)
          mid-point (/ garden-length 2)]
      [(subs garden 0 mid-point)
       (subs garden mid-point garden-length)])
    )
  )

(def map-character-to-plant {\G "grass" \C "clover" \R "radishes" \V "violets"})

(defn get-student-plants [garden students student]
  (let [garden (normalise-garden-input garden)
        students (sort (vec students))
        [row1 row2] garden
        index (.indexOf students student)
        start (* index 2)
        end (+ start 2)
        [row1 row2] garden
        student-cups (concat (subs row1 start end) (subs row2 start end))]
    (map map-character-to-plant student-cups)))