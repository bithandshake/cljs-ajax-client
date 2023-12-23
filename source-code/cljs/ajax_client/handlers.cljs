
(ns ajax-client.handlers
    (:require [ajax-client.state :as state]
              [ajax-client.utils :as utils]))

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
  (fn [server-response]
      (swap! state/REFERENCES dissoc request-id)
      (if (fn? error-handler-f)
          (if (fn? response-parser-f)
              (let [server-response (update server-response :response #(response-parser-f request-id %))]
                   (error-handler-f request-id server-response))
              (error-handler-f request-id server-response)))))

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
  (fn [server-response-body]
      (swap! state/REFERENCES dissoc request-id)
      (if (fn? response-handler-f)
          (if (fn? response-parser-f)
              (let [server-response-body (response-parser-f request-id server-response-body)]
                   (response-handler-f request-id server-response-body))
              (response-handler-f request-id server-response-body)))))

(defn progress-handler-f
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:progress-handler-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [progress-handler-f]}]
  (fn [progress-event]
      (if (fn? progress-handler-f)
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
