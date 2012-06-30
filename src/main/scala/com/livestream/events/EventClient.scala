package com.livestream.events

import java.net.Socket

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

class EventClient(
  val host: String,
  val port: Int
) {

  val sock = new Socket(host, port)

  val sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream))
  val sockOut = new PrintWriter(sock.getOutputStream)

  def startPoll(
    eventName: String, 
    pollInterval: Int,
    eventStatusCallback: (String,Boolean) => Unit
  ) {
  }

  def stopPoll {
  }

  def ping: Boolean = {
    sockOut.println(Commands.PING)
    sockIn.readLine match {
      case Commands.PONG => true
      case _ => false
    }
  }

  def getLiveStatus(eventName: String): Boolean = {
    sockOut.println(Commands.GET_LIVESTATUS)
    sockIn.readLine match {
      case Commands.SEND_LIVESTATUS =>
        sockIn.readLine.toBoolean
      case Commands.ERROR =>
        val errorMsg = sockIn.readLine
        println(errorMsg)
        false
      case _ =>
        println("UnknownCommand")
        false
    }
  }

  def close {
    try { sockIn.close } catch { case ex: Exception => ex.printStackTrace }
    try { sockOut.close } catch { case ex: Exception => ex.printStackTrace }
    try { sock.close } catch { case ex: Exception => ex.printStackTrace }
  }
}
