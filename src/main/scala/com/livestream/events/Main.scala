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

  Thread.sleep(3600 * 1000)


/*if(args.length != 2) {
  println("required args, in seconds: [poll-interval] [run-time]")
} else {
  val pollInterval = args(0).toInt
  val runTime = args(1).toInt

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

  val eventClient = new EventClient("localhost", 9999)
  eventClient.startPoll(
    "Wimbledon Day 5",
    pollInterval * 1000,
    eventHandler
  )
 
  Thread.sleep(runTime * 1000)

  eventClient.stopPoll
  eventClient.close

  eventServer.stop
  println("Done")
}*/
}
