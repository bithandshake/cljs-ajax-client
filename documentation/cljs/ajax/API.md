
# <strong>ajax.api</strong> namespace
<p>Documentation of the <strong>ajax/api.cljs</strong> file</p>

<strong>[README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > ajax.api</strong>



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
(ns my-namespace (:require [ajax.api :as ajax :refer [abort-request!]]))

(ajax/abort-request! ...)
(abort-request!      ...)
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
(ns my-namespace (:require [ajax.api :as ajax :refer [progress-event->request-progress]]))

(ajax/progress-event->request-progress ...)
(progress-event->request-progress      ...)
```

</details>

---

### request->local-request?

```
@param (map) request
```

```
@usage
(request->local-request {:uri "..."})
```

```
@example
(request->local-request {:uri "/my-route"})
=>
true
```

```
@example
(request->local-request {:uri "https://my-site.com"})
=>
false
```

```
@return (boolean)
```

<details>
<summary>Source code</summary>

```
(defn request->local-request?
  [{:keys [uri]}]
  (let [uri-external? (re-find #"^\w+?://" uri)]
       (not uri-external?)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [ajax.api :as ajax :refer [request->local-request?]]))

(ajax/request->local-request? ...)
(request->local-request?      ...)
```

</details>

---

### send-request!

```
@param (keyword) request-id
@param (map) request-props
```

```
@usage
(defn my-response-handler-f [request-id server-response])
(send-request! :my-request {:method             :post
                            :response-handler-f my-response-handler-f
                            :uri                "/my-uri"})
```

<details>
<summary>Source code</summary>

```
(defn send-request!
  [request-id {:keys [method uri] :as request-props}]
  (let [reference (case method :get  (core/GET  uri (prototypes/GET-request-props-prototype  request-id request-props))
                               :post (core/POST uri (prototypes/POST-request-props-prototype request-id request-props)))]
       (swap! state/REQUESTS assoc request-id reference)))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [ajax.api :as ajax :refer [send-request!]]))

(ajax/send-request! ...)
(send-request!      ...)
```

</details>
