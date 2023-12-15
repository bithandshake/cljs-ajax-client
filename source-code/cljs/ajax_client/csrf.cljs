
(ns ajax-client.csrf
    (:require [ajax.core]
              [ajax-client.config :as config]
              [ajax-client.utils  :as utils]))

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
  (let [interceptor (ajax.core/to-interceptor {:name "default headers" :request default-headers})]
       (swap! ajax.core/default-interceptors conj interceptor)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; ...
(load-interceptors!)
