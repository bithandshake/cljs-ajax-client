
(ns ajax.handlers
    (:require [ajax.state :as state]
              [ajax.utils :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-handler-f
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:error-handler-f (function)(opt)
  ;  :response-parser-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [error-handler-f response-parser-f]}]
  (if error-handler-f (fn [server-response]
                          (swap! state/REFERENCES dissoc request-id)
                          (if response-parser-f (let [server-response (update server-response :response #(response-parser-f request-id %))]
                                                     (error-handler-f request-id server-response))
                                                (error-handler-f request-id server-response)))
                      (fn [_]
                          (swap! state/REFERENCES dissoc request-id))))

(defn response-handler-f
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:response-handler-f (function)(opt)
  ;  :response-parser-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [response-handler-f response-parser-f]}]
  (if response-handler-f (fn [server-response-body]
                             (swap! state/REFERENCES dissoc request-id)
                             (if response-parser-f (let [server-response-body (response-parser-f request-id server-response-body)]
                                                        (response-handler-f request-id server-response-body))
                                                   (response-handler-f request-id server-response-body)))
                         (fn [_]
                             (swap! state/REFERENCES dissoc request-id))))

(defn progress-handler-f
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:progress-handler-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [progress-handler-f]}]
  (if progress-handler-f (fn [progress-event]
                             (let [request-progress (utils/progress-event->request-progress progress-event)]
                                  (progress-handler-f request-id request-progress)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-handlers
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;
  ; @return (map)
  ; {:error-handler (function)
  ;  :handler (function)
  ;  :progress-handler (function)}
  [request-id request-props]
  {:error-handler    (error-handler-f    request-id request-props)
   :handler          (response-handler-f request-id request-props)
   :progress-handler (progress-handler-f request-id request-props)})
