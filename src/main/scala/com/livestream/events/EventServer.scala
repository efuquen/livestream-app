package com.livestream.events

import java.net.{ServerSocket,Socket}

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

import java.util.concurrent.{ThreadPoolExecutor, TimeUnit, LinkedBlockingQueue}

import akka.actor._

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

  val actorSystem = ActorSystem("EventServerActors")
  val eventActor = actorSystem.actorOf(Props[EventActor], name="eventActor")

  val serverThread = new Thread( new Runnable { def run {
    println("Event Server Started")
    while(!Thread.interrupted) {
      try {
        val sock = server.accept
        val sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream))
        val sockOut = new PrintWriter(sock.getOutputStream)
        actorSystem.actorOf(Props(
          new SocketActor(sock, sockIn, sockOut, eventActor)
        ))
      } catch {
        case ex: Exception =>
          Thread.currentThread.interrupt
      }
    }
    println("Event Server Stopped")
  }})


  def start { serverThread.start }
  def stop { server.close }
}
