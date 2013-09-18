package com.madebymira.twitter

import play.api.mvc._

trait TwitterConfig {
    self: Controller =>

    val twConsumerKey: String
    val twConsumerSecret: String
    val twCallbackURL: String
}