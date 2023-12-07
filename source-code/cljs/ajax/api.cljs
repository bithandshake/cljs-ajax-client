
(ns ajax.api
    (:require [ajax.csrf]
              [ajax.side-effects :as side-effects]
              [ajax.utils        :as utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @redirect (ajax.side-effects)
(def send-request!  side-effects/send-request!)
(def abort-request! side-effects/abort-request!)

; @redirect (ajax.utils)
(def progress-event->request-progress utils/progress-event->request-progress)
(def request->internal-request?       utils/request->internal-request?)
