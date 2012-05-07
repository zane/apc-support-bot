apc-support-bot
===============

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
(use 'apc-support-bot.core)
make-oauth-consumer
(def oauth-consumer (make-oauth-consumer "..." "..."))
(def request-token (oauth/request-token oauth-consumer))
(oauth/user-approval-uri oauth-consumer (:oauth_token request-token))
(def access-token-response (oauth/access-token oauth-consumer request-token ...))
```