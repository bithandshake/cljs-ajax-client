
(ns ajax.helpers
    (:require [mid-fruits.math :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn progress-event->request-progress
  ; @param (object) progress-event
  ;
  ; @usage
  ;  (progress-event->request-progress %)
  ;
  ; @return (percent)
  [progress-event]
  (let [loaded (.-loaded progress-event)
        total  (.-total  progress-event)]
       (math/percent total loaded)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->local-request?
  ; @param (map) request
  ;  {:uri (string)}
  ;
  ; @usage
  ;  (request->local-request {:uri "..."})
  ;
  ; @return (boolean)
  [{:keys [uri]}]
  (let [uri-external? (re-find #"^\w+?://" uri)]
       (not uri-external?)))
