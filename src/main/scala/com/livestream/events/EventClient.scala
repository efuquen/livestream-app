package com.livestream.events

import java.net.Socket

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

class EventClient(
  val host: String,
  val port: Int,
  val eventName: String,
  val pollingInterval: Long  //In ms
) {

  def start() {
    val sock = new Socket(host, port)

    while(!Thread.interrupted) {
      val sockIn = new BufferedReader(new InputStreamReader(sock.getInputStream))
      val sockOut = new PrintWriter(sock.getOutputStream)
    }
  }
}
