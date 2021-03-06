(ns boodle.api.resources.expense
  (:require [boodle.model.expenses :as model]
            [boodle.utils.dates :as ud]
            [boodle.utils.numbers :as numbers]
            [boodle.utils.resource :as ur]
            [compojure.core :refer [context defroutes DELETE GET POST PUT]]
            [ring.util.http-response :as response]))

(defn find-all
  []
  (model/select-all))

(defn find-by-id
  [id]
  (-> id
      numbers/str->integer
      model/select-by-id))

(defn find-by-item
  [item]
  (model/select-by-item item))

(defn find-by-date-and-categories
  [request]
  (let [{from :from to :to categories :categories}
        (ur/request-body->map request)
        from (ud/to-local-date from)
        to (if (nil? to) (ud/today) (ud/to-local-date to))
        cs (numbers/strs->integers categories)]
    (model/select-by-date-and-categories from to cs)))

(defn insert!
  [request]
  (-> request
      ur/request-body->map
      (numbers/record-str->double :amount)
      (numbers/record-str->double :id-category)
      (ud/record-str->record-date :date)
      (model/insert!)))

(defn update!
  [request]
  (-> request
      ur/request-body->map
      (numbers/record-str->double :amount)
      (numbers/record-str->double :id-category)
      (ud/record-str->record-date :date)
      (model/update!)))

(defn delete!
  [id]
  (-> id
      numbers/str->integer
      model/delete!))

(defroutes routes
  (context "/api/expense" [id]
    (GET "/find" []
      (response/ok (find-all)))
    (GET "/find/:id" [id]
      (response/ok (find-by-id id)))
    (POST "/find-by-date-and-categories" request
      (response/ok (find-by-date-and-categories request)))
    (POST "/insert" request
      (response/ok (insert! request)))
    (PUT "/update" request
      (response/ok (update! request)))
    (DELETE "/delete/:id" [id]
      (response/ok (delete! id)))))
