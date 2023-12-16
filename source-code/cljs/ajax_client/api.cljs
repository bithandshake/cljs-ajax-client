
(ns ajax-client.api
    (:require [ajax-client.csrf]
              [ajax-client.side-effects :as side-effects]
              [ajax-client.utils        :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @tutorial CSRF token
;
; To use a CSRF token in request headers, place an element into the DOM tree with
; the `data-csrf-token` attribute, and set the actual token as its value.
;
; @usage
; [:html [:body [:div {:data-csrf-token "..."}]]]

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (ajax-client.side-effects/*)
(def send-request!  side-effects/send-request!)
(def abort-request! side-effects/abort-request!)

; @redirect (ajax-client.utils/*)
(def progress-event->request-progress utils/progress-event->request-progress)
(def request->internal-request?       utils/request->internal-request?)
