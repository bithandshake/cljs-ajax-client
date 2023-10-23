
(ns ajax.csrf
    (:require [ajax.config :as config]
              [ajax.core   :as core]
              [ajax.utils  :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn default-headers
  ; @ignore
  ;
  ; @param (map) request
  ;
  ; @return (map)
  ; {:headers (map)
  ;   {"x-csrf-token" (string)}}
  [request]
  (if (utils/request->internal-request? request)
      (update request :headers merge {"x-csrf-token" config/CSRF-TOKEN})
      (->     request)))

(defn load-interceptors!
  ; @ignore
  []
  (let [interceptor (core/to-interceptor {:name "default headers" :request default-headers})]
       (swap! core/default-interceptors conj interceptor)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; ...
(load-interceptors!)
