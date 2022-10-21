
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns ajax.sample
    (:require [ajax.api :as ajax]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-response-handler-f
  [request-id server-response])

(defn send-my-request!
  []
  (ajax/send-request! :my-request
                      {:method             :post
                       :response-handler-f my-response-handler-f
                       :uri                "/my-uri"}))

(defn abort-my-request!
  []
  (ajax/abort-request! :my-request))
