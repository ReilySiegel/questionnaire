(ns com.reilysiegel.questionnaire
  (:require [com.reilysiegel.questionnaire.question :as question]
            [com.reilysiegel.questionnaire.option :as option]))

(defn questionnaire [question & questions]
  {::queue    questions
   ::question question
   ::results  []})

(defn answer [questionnaire option-id]

  (if-not (question/contains? (::question questionnaire) option-id)
    questionnaire
    (let [option (first (filter (comp #{option-id} ::option/id)
                                (-> questionnaire ::question question/options)))
          queue  (into (::queue questionnaire) (::option/questions option))]
      questionnaire
      (assoc questionnaire
             ::queue (rest queue)
             ::question (first queue)
             ::results  (conj (::results questionnaire)
                              (merge (dissoc option ::option/questions)
                                     (dissoc (::question questionnaire)
                                             ::question/options)))))))

(comment
  (def color {::question/id      :color
              ::question/options [{::option/id :red}]})
  (def square {:question/id       :square
               ::question/options [{::option/id true}
                                   {::option/id false}]})
  (def shape {:question/id       :shape
              ::question/options [{::option/id :round}
                                  {::option/id        :rectangle
                                   ::option/questions [square]}]})
  (def testionnaire (questionnaire color shape))
  (question/options (::question testionnaire))
  (-> testionnaire
      (answer :red)))
