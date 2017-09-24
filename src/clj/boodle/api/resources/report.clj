(ns boodle.api.resources.report
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as dates]
            [boodle.utils.numbers :as numbers]))

(defn get-data
  [params]
  (let [{from :from to :to categories :categories} params
        to (if (nil? to) (dates/today) to)
        expenses (model/select-by-date-and-categories from to categories)
        total (apply + (map :amount expenses))
        data (map numbers/convert-amount expenses)]
    (-> {}
        (assoc :data data)
        (assoc :total (numbers/en->ita total)))))
