(ns boodle.savings.views
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.modal :as modal]
            [boodle.savings.views.achieved-aims :as achieved]
            [boodle.savings.views.active-aims :as active]
            [boodle.savings.views.savings :as savings]
            [re-frame.core :as rf]))

(defn total
  []
  (fn []
    (let [savings @(rf/subscribe [:savings])
          total-savings (:total savings)
          aims @(rf/subscribe [:aims-summary])
          total-aims (:total aims)]
      [:div {:style {:text-align "center" :margin-top "-0.8em"}}
       [:h5 (translate :it :savings/label.total)
        [:strong (str (common/format-number (+ total-savings total-aims))
                      (translate :it :currency))]]])))

(defn home-panel
  []
  (fn []
    [:div
     [common/header]
     [:div.container {:style {:margin-top "1em"}}
      [common/page-title (translate :it :savings/page.title)]
      [savings/total]
      [savings/table]
      [savings/buttons]
      [:hr]

      [common/page-title (translate :it :aims/page.title)]
      [active/total]
      [active/dropdown]
      [active/buttons]

      [modal/modal]
      [:hr]

      [active/table]
      [total]
      [:hr]

      [common/page-title (translate :it :aims/label.archive)]
      [achieved/dropdown]
      [:div {:style {:padding-top "1em" :padding-bottom ".1em"}}
       [achieved/table]]]]))
