package com.livestream.events

import java.net.ServerSocket 

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

/**
 * All events will be modeled  here
**/
case class Event(
  val name: String,
  val url: String
)

class EventServer(
  events: Map[String,Event],
  port: Int
){

  def start() = {
    val server = new ServerSocket(port)
    val eventActor = new EventActor

    println("Event Server started")
    while(!Thread.interrupted) {
      val sock = server.accept
      val sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream))
      val sockOut = new PrintWriter(sock.getOutputStream)
      try {
        var keepAlive = true
        while(keepAlive && !Thread.interrupted) {
          val command = sockIn.readLine
          //Switch to correct action based on command
          command match {
            case Commands.PING =>
              sockOut.println(Commands.PONG)
              sockOut.flush
            case Commands.GET_LIVESTATUS =>
              val eventName = sockIn.readLine
              if(events.contains(eventName)) {
                val event = events(eventName)
                val isLive = eventActor.isEventLive(event)
                sockOut.println(Commands.SEND_LIVESTATUS)
                sockOut.println(isLive)
                sockOut.flush
              } else {
                sockOut.println(Commands.ERROR)
                sockOut.println("EventDNE")
                sockOut.flush
              }
            //Any other command will close the connection
            case _ =>
              keepAlive = false
          }
        }
      } catch {
        case ex: InterruptedException =>
          Thread.currentThread.interrupt 
        case ex: Exception =>
          ex.printStackTrace
      } finally {
        try { sockIn.close } catch { case ex: Exception => ex.printStackTrace }
        try { sockOut.close } catch { case ex: Exception => ex.printStackTrace }
        try { sock.close } catch { case ex: Exception => ex.printStackTrace }
      }
    }

    println("Event Server stopped")
  }
}
