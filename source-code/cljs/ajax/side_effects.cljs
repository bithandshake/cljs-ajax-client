
(ns ajax.side-effects
    (:require [ajax.core       :as core]
              [ajax.prototypes :as prototypes]
              [ajax.state      :as state]
              [random.api      :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn send-request!
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
  ;  :timeout (ms)(opt)
  ;  :uri (string)}
  ;
  ; @usage
  ; (defn my-progress-handler-f [request-id request-progress])
  ; (defn my-error-handler-f    [request-id server-response])
  ; (defn my-response-handler-f [request-id server-response])
  ; (send-request! :my-request {:method             :post
  ;                             :progress-handler-f my-progress-handler-f
  ;                             :error-handler-f    my-error-handler-f
  ;                             :response-handler-f my-response-handler-f
  ;                             :uri                "/my-uri"})
  ;
  ; @return (keyword)
  ([request-props]
   (send-request! (random/generate-keyword) request-props))

  ([request-id {:keys [method uri] :as request-props}]
   (let [reference (case method :get  (core/GET  uri (prototypes/GET-request-props-prototype  request-id request-props))
                                :post (core/POST uri (prototypes/POST-request-props-prototype request-id request-props)))]
        (swap! state/REQUESTS assoc request-id reference)
        (-> request-id))))

(defn abort-request!
  ; @param (keyword) request-id
  ;
  ; @usage
  ; (abort-request! :my-request)
  [request-id]
  (let [reference (get @state/REQUESTS request-id)]
       (core/abort reference)))
