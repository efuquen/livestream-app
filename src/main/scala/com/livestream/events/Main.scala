package com.livestream.events

import java.util.Date

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

  //hack, make sure event server starts
  Thread.sleep(2000)

  val eventClient = new EventClient("localhost", 9999)
  eventClient.startPoll(
    "Wimbledon Day 5",
    1000 * 5,
    (eventName, isLive) => 
      println("%s - Event:'%s' Status: %s".format(new Date, eventName, isLive))
  )

  Thread.sleep(16 * 1000)

  println("Kill client & server")
  eventClient.stopPoll
  eventClient.close
  eventServer.stop
  println("Done")
}
