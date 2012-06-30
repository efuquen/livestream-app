package com.livestream.events

/**
 * constants for possible commands
 **/
object Commands {
  val PING = "PING"
  val PONG = "PONG"
  val GET_LIVESTATUS = "GET_LIVESTATUS"
  val SEND_LIVESTATUS = "SEND_LIVESTATUS"
  val ERROR = "ERROR"
  val CLOSE = "CLOSE"
}
