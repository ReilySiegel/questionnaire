(ns com.reilysiegel.questionnaire
  (:require [com.reilysiegel.questionnaire.question :as question]
            [com.reilysiegel.questionnaire.option :as option]))

(defn questionnaire [question & questions]
  {::queue    questions
   ::question question
   ::results  []})

(defn answer [{::keys [queue question results]
               :as    questionnaire}
              {::option/keys [questions]
               :as           option}]
  ;; Do nothing if option is not is question
  (if-not ((set (::question/options question)) option)
    questionnaire
    (let [queue* (into queue questions)]
      {::queue    (rest queue*)
       ::question (first queue*)
       ::results  (conj results option)})))

(comment
  (def color-options [{::option/id :red}])
  (def color {::question/id      :color
              ::question/options color-options})
  (def square-options [{::option/id true}
                       {::option/id false}])
  (def square {:question/id       :square
               ::question/options square-options})
  (def shape-options [{::option/id :round}
                      {::option/id        :rectangle
                       ::option/questions [square]}])
  (def shape {:question/id       :shape
              ::question/options shape-options})
  (def testionnaire (questionnaire color shape))
  (::question/options (::question testionnaire))
  (= testionnaire (answer testionnaire {:test :test}))
  (-> testionnaire
      (answer (first color-options))
      (answer (second shape-options))))
