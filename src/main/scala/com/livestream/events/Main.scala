package com.livestream.events

object Main extends App {
  val eventServer = new EventServer(
    Map[String,Event](
      "Wimbledon Day 5" -> new Event(
        "Wimbledon Day 5",
        "http://api.new.livestream.com/accounts/Wimbledon/events/Day5")
    ),
    9999
  )

  eventServer.start

  val eventClient = new EventClient("localhost", 9999)
}
