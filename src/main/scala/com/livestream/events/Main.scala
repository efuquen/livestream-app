package com.livestream.events

import java.util.Date

/**
 *  Launcher Object.  Will run in two modes
 *  
 *  server - will run just the server, can telnet to run commands
 *
 *  client - will run server and then a test client to poll at interval
 *           for a set amount of time.
 **/
object Main extends App {
if(args.length == 0) {
  println("must at least one arg: server|demo")
} else {
  args(0) match {
    case "server" =>
      val eventServer = new EventServer(
        Map[String,Event](
          "Wimbledon Day 5" -> new Event(
            "Wimbledon Day 5",
            "http://api.new.livestream.com/accounts/Wimbledon/events/Day5")
        ),
        9999
      )

      eventServer.start
    case "demo" =>
      if(args.length != 3) {
        println("required args for demo mode, in seconds: [poll-interval] [run-time]")
      } else {

        val pollInterval = args(1).toInt
        val runTime = args(2).toInt

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
      }
    case _ =>
      println("invalid mode: %s".format(args(0)))
  }
}
}
