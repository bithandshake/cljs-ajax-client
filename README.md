
# ajax-api

> "I show you how deep the rabbit hole goes" – Morpheus

### Overview

The <strong>ajax-api</strong> is an ansynchcronous ClojureScript request handler
based on the [JulianBirch / cljs-ajax] library with some extra features such as
CSRF handling, etc.

### deps.edn

```
{:deps {bithandshake/ajax-api {:git/url "https://github.com/bithandshake/ajax-api"
                               :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/bithandshake/ajax-api/tree/release).

### Documentation

The <strong>ajax-api</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>ajax-api</strong> library [here](CHANGES.md).

# Usage

> Some functions and some parameters of the following functions won't be discussed.
  To learn more about the available functionality, check out the
  [functional documentation](documentation/COVER.md)!

### How to send a request?

The [`ajax.api/send-request!`](documentation/cljs/ajax/API.md#send-request) function
sends an AJAX request to the given uri by the given method.

```
(send-request! {:method :post
                :uri    "/my-uri"})
```

You can set a progress handler function which takes the request-id and the current
progress value as its parameters.

```
(defn my-progress-handler-f
  [request-id request-progress]
  (println "This request is" request-progress "% done."))

(send-request! {:method             :post
                :progress-handler-f my-progress-handler-f
                :uri                "/my-uri"})
```

Both the error handler and the response handler function takes the request-id
and the server response as its parameters.

```
(defn my-error-handler-f
  [request-id server-response]
  (println "Something went wrong:" server-response))

(defn my-response-handler-f
  [request-id server-response]
  (println "I got the answer:" server-response))

(send-request! {:method             :post
                :error-handler-f    my-error-handler-f
                :response-handler-f my-response-handler-f
                :uri                "/my-uri"})
```

### How to abort an ongoing request?

The [`ajax.api/abort-request!`](documentation/cljs/ajax/API.md#abort-request)
function immediately stops the ongoing request with the given id.

First you should do is that to send the request with a specified id what can
be useful when you want to abort your request.

```
(send-request! :my-request
               {:method :post
                :uri    "/my-uri"})

(abort-request! :my-request)
```

An alternative way is to keep the return value of the
[`ajax.api/send-request!`](documentation/cljs/ajax/API.md#send-request) function
which is the id of the sent request.

```
(def REQUEST-ID (send-request! {:method :post :uri "/my-uri"}))

(abort-request! REQUEST-ID)
```

### Re-Frame events

The [`ajax.api/send-request!`](documentation/cljs/ajax/API.md#send-request) and
[`ajax.api/abort-request!`](documentation/cljs/ajax/API.md#send-request) functions
are also registered as Re-Frame side-effect events.

> In the following examples you see Re-Frame event dispatching in a slightly
  different way. That's because the <strong>ajax-api</strong> library uses not
  the original Re-Frame library.  

> Learn more: https://github.com/bithandshake/re-frame-api

```
(re-frame.api/reg-event-fx :send-my-request!
  (fn [_ _]
      {:fx [:ajax/send-request! {...}]}))
```

```
(re-frame.api/dispatch {:fx [:ajax/send-request! {...}]})
```

```
(re-frame.api/reg-event-fx :send-my-request!
  (fn [_ _]
      {:fx [:ajax/send-request! :my-request {...}]}))
```

```
(re-frame.api/dispatch {:fx [:ajax/send-request! :my-request {...}]})
```

```
(re-frame.api/reg-event-fx :send-my-request!
  (fn [_ _]
      {:fx [:ajax/abort-request! :my-request]}))
```

```
(re-frame.api/dispatch {:fx [:ajax/abort-request! :my-request]})
```


### CSRF token

To use a CSRF token in request headers, you have to place an element in the DOM
tree with the `data-csrf-token` attribute and to set the actual token as its value.

```
[:div {:data-csrf-token "..."}]
```
