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
  events: List[Event],
  port: Int
){
  val server = new ServerSocket(port)

  while(true) {
    val sock = server.accept
    val sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream))
    val sockOut = new PrintWriter(sock.getOutputStream)

    val command = sockIn.readLine

    //Switch to correct action based on command
    command match {
      case "PING" =>
      case "GET_LIVESTATUS" =>
    }
  }
}
