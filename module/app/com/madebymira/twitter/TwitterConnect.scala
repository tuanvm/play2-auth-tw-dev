package com.madebymira.twitter

import play.api.{ Logger, Application, Plugin, Routes }
import play.api.libs._
import play.api.libs.ws.WS
import scala.concurrent._
import scala.concurrent.duration._
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;
import twitter4j.TwitterFactory;
import twitter4j.auth._;
import twitter4j.TwitterException
import java.io.BufferedReader
import java.io.InputStreamReader

trait TwitterConnect {
    self: TwitterConfig =>

    /**
     * Gets the twitter request token.
     *
     * @return the twitter request token
     */
    def getTwitterRequestToken: RequestToken = {
        val twitter: Twitter = new TwitterFactory().getInstance;
        twitter.setOAuthConsumer(twConsumerKey, twConsumerSecret);
        return twitter.getOAuthRequestToken();
    }

    /**
     * Gets the twitter authorize url.
     *
     * @param requestToken the request token
     * @return the twitter authorize url
     */
    def getTwitterAuthorizeURL(requestToken: RequestToken): String = {
        requestToken.getAuthorizationURL()
    }

    /**
     * Gets the twitter access token.
     *
     * @param requestToken the request token
     * @param verifier the verifier
     * @return the twitter access token
     */
    def getTwitterAccessToken(requestToken: RequestToken, verifier: String): AccessToken = {
        val twitter: Twitter = new TwitterFactory().getInstance;
        twitter.setOAuthConsumer(twConsumerKey, twConsumerSecret);
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
        } catch {
            case e: Exception => {
                Logger.error("TwitterConnect module ----- Can't getOAuthAccessToken")
                return null;
            }
        }
    }

    /**
     * Gets the twitter user.
     *
     * @param accessToken the access token
     * @param id the id
     * @return the twitter user
     */
    def getTwitterUser(accessToken: AccessToken, id: Long): User = {
        val twitter: Twitter = new TwitterFactory().getInstance;
        twitter.setOAuthConsumer(twConsumerKey, twConsumerSecret);
        twitter.setOAuthAccessToken(accessToken)
        twitter.showUser(id)
    }
}