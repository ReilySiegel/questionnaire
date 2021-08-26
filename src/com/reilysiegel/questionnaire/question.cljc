(ns com.reilysiegel.questionnaire.question
  (:require [com.reilysiegel.questionnaire.option :as option])
  (:refer-clojure :exclude [contains?]))

(defn contains? [question option-id]
  (clojure.core/contains? 
   (into #{} (map ::option/id)
         (::options question))
   option-id))
