
(ns ajax.prototypes
    (:require [ajax.helpers :as helpers]
              [ajax.state   :as state]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn error-handler-f
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:error-handler-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [error-handler-f]}]
  (if error-handler-f (fn [server-response]
                          (swap! state/REQUESTS dissoc request-id)
                          (error-handler-f request-id server-response))
                      (fn [_]
                          (swap! state/REQUESTS dissoc request-id))))

(defn response-handler-f
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:response-handler-f (function)(opt)}
  ;
  ; @return (function)
  [request-id {:keys [response-handler-f]}]
  (if response-handler-f (fn [server-response]
                             (swap! state/REQUESTS dissoc request-id)
                             (response-handler-f request-id server-response))
                         (fn [_]
                             (swap! state/REQUESTS dissoc request-id))))

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
                             (let [request-progress (helpers/progress-event->request-progress progress-event)]
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

(defn GET-request-props-prototype
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:uri (string)}
  ;
  ; @return (map)
  ; {:error-handler (function)
  ;  :handler (function)
  ;  :progress-handler (function)
  ;  :uri (string)}
  [request-id request-props]
  (merge (request-handlers request-id request-props)
         (select-keys request-props [:uri])))

(defn POST-request-props-prototype
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:body (*)
  ;  :params (map)
  ;  :uri (string)}
  ;
  ; @return (map)
  ; {:body (*)
  ;  :error-handler (function)
  ;  :handler (function)
  ;  :params (map)
  ;  :progress-handler (function)
  ;  :uri (string)}
  [request-id request-props]
  (merge (request-handlers request-id request-props)
         (select-keys request-props [:body :params :uri])))
