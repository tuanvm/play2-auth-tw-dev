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

/**
 * The Class TwitterPlugin.
 */
class TwitterPlugin(application: Application) extends Plugin {
    val TWITTER_CONSUMER_KEY: String = "twitter.consumer-key";
    val TWITTER_CONSUMER_SECRET: String = "twitter.consumer-secret";
    val TWITTER_CALLBACK_URL: String = "twitter.callbackURL"

    lazy val consumerKey: String = application.configuration.getString(TWITTER_CONSUMER_KEY).getOrElse(null);
    lazy val consumerSecret: String = application.configuration.getString(TWITTER_CONSUMER_SECRET).getOrElse(null);

    lazy val callbackURL: String = application.configuration.getString(TWITTER_CALLBACK_URL).getOrElse(null);

    /* (non-Javadoc)
     * @see play.api.Plugin#onStart()
     */
    override def onStart() {
        val configuration = application.configuration;
        // you can now access the application.conf settings, including any custom ones you have added

        Logger.info("TwitterPlugin has started");
    }

    def getRequestToken: RequestToken = {
        val twitter: Twitter = new TwitterFactory().getInstance;
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        return twitter.getOAuthRequestToken();
    }

    def getAuthorizeURL(requestToken: RequestToken): String = {
        requestToken.getAuthorizationURL()
    }

    def getAccessToken(requestToken: RequestToken, verifier: String): AccessToken = {
        val twitter: Twitter = new TwitterFactory().getInstance;
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
        } catch {
            case e: Exception => {
                Logger.error("Can't getOAuthAccessToken")
                return null;
            }
        }

    }

    def getTwitterUser(accessToken: AccessToken, id: Long): User = {
        val twitter: Twitter = new TwitterFactory().getInstance;
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        twitter.setOAuthAccessToken(accessToken)
        twitter.showUser(id)
    }
}