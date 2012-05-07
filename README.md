# A.P.C. Support Bot

A silly little bot to give encouragement to people struggling with wearing in their raw A.P.C. denim.

## Getting Started

### Configuration

The bot expects a configuration file in the top-level of the project directory named `config.clj` with the following structure.

```Clojure
{:app-consumer-key "..."
 :app-consumer-secret "..."
 :user-access-token "..."
 :user-access-token-secret "..."
 :user-id 012345
 :screen-name "..."}
```

If you don't already have an access token, use the following series of commands to create one.

```Clojure
(defn make-oauth-consumer
  [consumer-key consumer-secret]
  (clj-oauth/make-consumer consumer-key
                       consumer-secret
                       "https://api.twitter.com/oauth/request_token"
                       "https://api.twitter.com/oauth/access_token"
                       "https://api.twitter.com/oauth/authorize"
                       :hmac-sha1))

(require ['oauth.client :as 'oauth])
(def oauth-consumer (make-oauth-consumer "..." "..."))
(def request-token (oauth/request-token oauth-consumer))
(oauth/user-approval-uri oauth-consumer (:oauth_token request-token))
(def access-token-response (oauth/access-token oauth-consumer request-token ...))
```

### Running

```Clojure
(def listener (listen))
```

### Stopping

```Clojure
((:cancel (meta listener)))
```
