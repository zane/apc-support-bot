(ns apc-support-bot.core
  (:use [useful.exception :only [rescue]])
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [twitter.oauth :as oauth]
            [twitter.api.restful :as restful]
            [twitter.api.streaming :as streaming]
            [twitter.callbacks.handlers :as handlers]
            [twitter.callbacks.protocols :as protocols])
  (:import (java.io PushbackReader)
           (twitter.callbacks.protocols AsyncStreamingCallback)))

(def config
  (binding [*read-eval* false]
    (with-open [r (io/reader "config.clj")]
      (read (PushbackReader. r)))))

(def creds
  (oauth/make-oauth-creds (:app-consumer-key config)
                          (:app-consumer-secret config)
                          (:user-access-token config)
                          (:user-access-token-secret config)))

(defn mentions?
  [message user-id]
  (not (nil? (some #(= (:id %) user-id)
                   (-> message :entities :user_mentions)))))

(defn streaming-json-body
  [response baos]
  (json/read-json (.toString baos)))

(defn tweet
  [message]
  (restful/update-status :oauth-creds creds
                         :params {:status message}))

(defn mention-screen-name
  [message]
  (-> message :user :screen_name))

(def print-mentions-callback
  (AsyncStreamingCallback.
   (fn [response baos]
     (let [entity (rescue (json/read-json (.toString baos)) nil)]
       (if (mentions? entity (:user-id config))
         (tweet (str "@" (mention-screen-name entity) " "
                     (let [responses (:responses config)]
                       (responses (rand-int (count responses)))))))))
   (comp println handlers/response-return-everything)
   handlers/exception-print))

(defn listen
  []
  (streaming/user-stream :oauth-creds creds
                         :callbacks print-mentions-callback))

(defn -main [& args]
  (listen))