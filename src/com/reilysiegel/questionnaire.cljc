(ns com.reilysiegel.questionnaire
  (:require [com.reilysiegel.questionnaire.question :as question]
            [com.reilysiegel.questionnaire.option :as option]))

(defn questionnaire [question & questions]
  {::queue    questions
   ::question question
   ::results  []})

(defn answer [{::keys [queue question results]
               :as    questionnaire}
              option-id]
  ;; Do nothing if option is not is question
  (if-not (question/contains? question option-id)
    questionnaire
    (let [option (first (filter (comp #{option-id} ::option/id)
                                (::question/options question)))
          queue  (into queue (::option/questions option))]
      (assoc questionnaire
             ::queue (rest queue)
             ::question (first queue)
             ::results  (conj results
                              ;; This keeps the data from both the question and
                              ;; option chosen in results, it is only necessary
                              ;; to keep the option chosen.
                              (merge (dissoc option ::option/questions)
                                     (dissoc question
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
  (::question/options (::question testionnaire))
  (-> testionnaire
      (answer :red)
      (answer :round)))
