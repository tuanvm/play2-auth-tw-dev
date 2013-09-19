This is your new Play 2.1 application
=====================================

This file will be packaged with your application, when using `play dist`.

How to use this module
1, run: play publish-local => to public this module to local

2, add "play2-auth-tw-module" % "play2-auth-tw-module_2.10" % "1.0-SNAPSHOT" to Build.scala in your project (with 2.10 is your play version => re-check your play version if needed)

3, create your gplus app, config callbackURL to your callback URL (example: http://localhost:9000/callback/tw)

4, add these lines to application.conf (and test.conf if needed), change these value properly
twitter.consumer-key=your consumer key
twitter.consumer-secret=your consumer secret
twitter.callbackURL="http://localhost:9000/callback/tw"

5, create social config trait (must extends Controller)
trait SocialConfig extends Controller with TwitterConfig {
    //Twitter configuration
    val twConsumerKey: String = Play.current.configuration.getString("twitter.consumer-key").getOrElse("")
    val twConsumerSecret: String = Play.current.configuration.getString("twitter.consumer-secret").getOrElse("")
    val twCallbackURL: String = Play.current.configuration.getString("twitter.callbackURL").getOrElse("")
}

6, let your controller which handle login/register with twitter connect extends SocialConfig, and with TwitterConnect (because SocialConfig extends Controller, so you no need to extends Controller anymore)
object SocialConnect extends SocialConfig with TwitterConnect{...}
 
7, click login/register with twitter button: 
- get request token by calling 
val requestToken = getTwitterRequestToken
- redirect user to
Redirect(getTwitterAuthorizeURL(requestToken))

8, implement a callback function to handle callbackURL, twitter will return a oauthVerifier or error (example: user denied), use requestToken and oauthVerifier to get access token (you must store requestToken to use in this step)
 val accessToken = getTwitterAccessToken(requestToken, oauthVerifier)

9. use this access token to get user info
val tuser = getTwitterUser(accessToken, accessToken.getUserId())

10. do your business logic and logged user in
