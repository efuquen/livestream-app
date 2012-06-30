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

  val eventHandler = (eventName: String, isLive: Boolean) => 
      println("%s - %s - Event:'%s' Status: %s".format(
        new Date, Thread.currentThread.getName, eventName, isLive))

  val eventClient1 = new EventClient("localhost", 9999)
  eventClient1.startPoll(
    "Wimbledon Day 5",
    5 * 1000,
    eventHandler
  )

  Thread.sleep((10 * 1000) + 1000)

  println("Start Second EventClient")
  val eventClient2 = new EventClient("localhost", 9999)
  eventClient2.startPoll(
    "Wimbledon Day 5",
    5 * 1000,
    eventHandler
  )

  Thread.sleep((10 * 1000) + 1000)

  println("Kill client & server")
  eventClient1.stopPoll
  eventClient1.close

  eventClient2.stopPoll
  eventClient2.close

  eventServer.stop
  println("Done")
}
