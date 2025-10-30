(ns c2030270-assessment.problem1)

(defn square-each-value-in-list [list]
    (map #(* % %) (filter number? list))
)