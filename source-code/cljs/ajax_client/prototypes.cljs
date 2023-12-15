
(ns ajax-client.prototypes
    (:require [ajax-client.handlers :as handlers]
              [ajax-client.state    :as state]
              [ajax-client.utils    :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request-props-prototype
  ; @ignore
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ; {:method (string)}
  ;
  ; @return (map)
  ; {:body (*)
  ;  :error-handler (function)
  ;  :handler (function)
  ;  :params (map)
  ;  :progress-handler (function)
  ;  :uri (string)}
  [request-id {:keys [method] :as request-props}]
  (merge (handlers/request-handlers request-id request-props)
         (case method :get  (select-keys request-props [:uri])
                      :post (select-keys request-props [:body :params :uri]))))
