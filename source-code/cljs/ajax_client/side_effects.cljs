
(ns ajax-client.side-effects
    (:require [ajax.core]
              [ajax-client.prototypes   :as prototypes]
              [ajax-client.state        :as state]
              [fruits.random.api :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-request!
  ; @description
  ; Sends an AJAX request to the given URI.
  ;
  ; @param (keyword)(opt) request-id
  ; @param (map) request-props
  ; {:error-handler-f (function)(opt)
  ;  :method (keyword)
  ;   :get, :post
  ;  :params (map)(opt)
  ;   W/ {:method :post}
  ;  :progress-handler-f (function)(opt)
  ;   W/ {:method :post}
  ;  :response-handler-f (function)(opt)
  ;  :response-parser-f (function)(opt)
  ;  :timeout (ms)(opt)
  ;  :uri (string)}
  ;
  ; @usage
  ; (defn my-progress-handler-f [request-id request-progress]     ...)
  ; (defn my-error-handler-f    [request-id server-response]      ...)
  ; (defn my-response-handler-f [request-id server-response-body] ...)
  ; (defn my-response-parser-f  [request-id server-response-body] (my-parser/read-json server-response-body))
  ; ...
  ; (send-request! :my-request {:method             :post
  ;                             :progress-handler-f my-progress-handler-f
  ;                             :error-handler-f    my-error-handler-f
  ;                             :response-parser-f  my-response-parser-f
  ;                             :response-handler-f my-response-handler-f
  ;                             :uri                "/my-uri"})
  ;
  ; @return (keyword)
  ([request-props]
   (send-request! (random/generate-keyword) request-props))

  ([request-id {:keys [method uri] :as request-props}]
   (let [reference (case method :get  (ajax.core/GET  uri (prototypes/request-props-prototype request-id request-props))
                                :post (ajax.core/POST uri (prototypes/request-props-prototype request-id request-props)))]
        (swap! state/REFERENCES assoc request-id reference)
        (-> request-id))))

(defn abort-request!
  ; @description
  ; Aborts an ongoing request identified by the given request ID.
  ;
  ; @param (keyword) request-id
  ;
  ; @usage
  ; (abort-request! :my-request)
  [request-id]
  (when-let [reference (-> state/REFERENCES deref request-id)]
            (swap! state/REFERENCES dissoc request-id)
            (ajax.core/abort reference)))
