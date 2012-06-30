package com.livestream.events

import java.util.Date

import java.net.Socket

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

class EventClient(
  val host: String,
  val port: Int
) {

  val sock = new Socket(host, port)

  val sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream))
  val sockOut = new PrintWriter(sock.getOutputStream)

  val lock = new Object
  var clientThread: Thread = null

  def startPoll(
    eventName: String, 
    pollInterval: Int,
    eventStatusCallback: (String,Boolean) => Unit
  ) { lock.synchronized {
    if(clientThread == null) {
      clientThread = new Thread(new Runnable { def run {
        if(ping) {
          while(!Thread.interrupted) {
            try {
              eventStatusCallback(eventName, getLiveStatus(eventName))
              Thread.sleep(pollInterval)
            } catch {
              case ex: InterruptedException =>
                println("Interrupt client")
                Thread.currentThread.interrupt
              case ex: Exception =>
                ex.printStackTrace
            }
          }
        } else {
          println("Ping unsucessful")
          lock.synchronized {
            clientThread = null
          }
        }
      }})
      clientThread.start
    } else {
      println("Must stop previous poll")
    }
  }}

  def stopPoll { this.synchronized {
    if(clientThread != null) {
      clientThread.interrupt
      clientThread = null
    }
  }}

  def ping: Boolean = {
    sockOut.println(Commands.PING)
    sockOut.flush
    sockIn.readLine match {
      case Commands.PONG => true
      case _ => false
    }
  }

  def getLiveStatus(eventName: String): Boolean = {
    sockOut.println(Commands.GET_LIVESTATUS)
    sockOut.println(eventName)
    sockOut.flush
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
