package com.livestream.events

import java.net.ServerSocket 

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

import java.util.concurrent.{ThreadPoolExecutor, TimeUnit, LinkedBlockingQueue}

/**
 * All events will be modeled  here
**/
case class Event(
  val name: String,
  val url: String
)

//EventServer not usable after stop
class EventServer(
  events: Map[String,Event],
  port: Int
) {
  
  val server = new ServerSocket(port)
  val eventActor = new EventActor

  val sockThreadPool = new ThreadPoolExecutor(
    20, 50, 60, TimeUnit.MINUTES, new LinkedBlockingQueue[Runnable]
  )

  val lock = new Object
  val serverThread = new Thread(new Runnable { def run {
    println("Event Server Started")
    while(!Thread.interrupted) {
      val sock = server.accept
      val sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream))
      val sockOut = new PrintWriter(sock.getOutputStream)
      sockThreadPool.execute(new Runnable { def run { try {
        var keepAlive = true
        while(keepAlive) {
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
        case ex: Exception =>
          Thread.currentThread.interrupt 
      } finally {
        try { sockIn.close } catch { case ex: Exception => ex.printStackTrace }
        try { sockOut.close } catch { case ex: Exception => ex.printStackTrace }
        try { sock.close } catch { case ex: Exception => ex.printStackTrace }
      }}})
    }
    println("Event Server Stopped")
  }})


  def start { serverThread.start }
  def stop { sockThreadPool.shutdown; server.close; }
}
