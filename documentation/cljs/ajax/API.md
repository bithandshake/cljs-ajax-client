
### ajax.api

Functional documentation of the ajax.api ClojureScript namespace

---

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > ajax.api

### Index

- [abort-request!](#abort-request)

- [progress-event->request-progress](#progress-event-request-progress)

- [request->internal-request?](#request-internal-request)

- [send-request!](#send-request)

---

### abort-request!

```
@param (keyword) request-id
```

```
@usage
(abort-request! :my-request)
```

<details>
<summary>Source code</summary>

```
(defn abort-request!
  [request-id]
  (let [reference (get @state/REQUESTS request-id)]
       (core/abort reference)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [ajax.api :refer [abort-request!]]))

(ajax.api/abort-request! ...)
(abort-request!          ...)
```

</details>

---

### progress-event->request-progress

```
@param (object) progress-event
```

```
@usage
(fn [progress-event]
    (progress-event->request-progress progress-event))
```

```
@return (percent)
```

<details>
<summary>Source code</summary>

```
(defn progress-event->request-progress
  [progress-event]
  (let [loaded (.-loaded progress-event)
        total  (.-total  progress-event)]
       (math/percent total loaded)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [ajax.api :refer [progress-event->request-progress]]))

(ajax.api/progress-event->request-progress ...)
(progress-event->request-progress          ...)
```

</details>

---

### request->internal-request?

```
@param (map) request
{:uri (string)}
```

```
@usage
(request->internal-request? {:uri "..."})
```

```
@example
(request->internal-request? {:uri "/my-route"})
=>
true
```

```
@example
(request->internal-request? {:uri "https://my-site.com"})
=>
false
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn request->internal-request?
  [{:keys [uri]}]
  (let [uri-external? (re-find #"^\w+?://" uri)]
       (not uri-external?)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [ajax.api :refer [request->internal-request?]]))

(ajax.api/request->internal-request? ...)
(request->internal-request?          ...)
```

</details>

---

### send-request!

```
@param (keyword)(opt) request-id
@param (map) request-props
{:error-handler-f (function)(opt)
 :method (keyword)
  :get, :post
 :params (map)(opt)
  W/ {:method :post}
 :progress-handler-f (function)(opt)
  W/ {:method :post}
 :response-handler-f (function)(opt)
 :timeout (ms)(opt)
 :uri (string)}
```

```
@usage
(defn my-progress-handler-f [request-id request-progress])
(defn my-error-handler-f    [request-id server-response])
(defn my-response-handler-f [request-id server-response])
(send-request! :my-request {:method             :post
                            :progress-handler-f my-progress-handler-f
                            :error-handler-f    my-error-handler-f
                            :response-handler-f my-response-handler-f
                            :uri                "/my-uri"})
```

```
@return (keyword)
```

<details>
<summary>Source code</summary>

```
(defn send-request!
  ([request-props]
   (send-request! (random/generate-keyword) request-props))

  ([request-id {:keys [method uri] :as request-props}]
   (let [reference (case method :get  (core/GET  uri (prototypes/GET-request-props-prototype  request-id request-props))
                                :post (core/POST uri (prototypes/POST-request-props-prototype request-id request-props)))]
        (swap! state/REQUESTS assoc request-id reference)
        (-> request-id))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [ajax.api :refer [send-request!]]))

(ajax.api/send-request! ...)
(send-request!          ...)
```

</details>

---

<sub>This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.</sub>

