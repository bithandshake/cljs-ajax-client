
(ns ajax.helpers
    (:require [math.api :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn progress-event->request-progress
  ; @param (object) progress-event
  ;
  ; @usage
  ; (fn [progress-event]
  ;     (progress-event->request-progress progress-event))
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
  ; {:uri (string)}
  ;
  ; @usage
  ; (request->local-request {:uri "..."})
  ;
  ; @example
  ; (request->local-request {:uri "/my-route"})
  ; =>
  ; true
  ;
  ; @example
  ; (request->local-request {:uri "https://my-site.com"})
  ; =>
  ; false
  ;
  ; @return (boolean)
  [{:keys [uri]}]
  (let [uri-external? (re-find #"^\w+?://" uri)]
       (not uri-external?)))
