package com.livestream.events

import java.net.URL

import java.io.StringWriter

import org.apache.commons.io.IOUtils

import com.codahale.jerkson.Json._

//import akka.actor.Actor

class EventActor { //extends Actor {

  private def getURLString(urlStr: String): String = {
    val url = new URL(urlStr)
    val urlIn = url.openStream
    try {
      val stringWriter = new StringWriter
      IOUtils.copy(urlIn, stringWriter)
      stringWriter.toString
    } finally {
      urlIn.close
    }
  }

  def isEventLive(event: Event): Boolean = {
    val jsonStr = getURLString(event.url)
    val jsonMap = parse[Map[String,Any]](jsonStr)
    val broadcastId = jsonMap("broadcast_id").toString.toInt
    broadcastId >= 0
  }

  /*def receive = {
    case ('GetEventLive, event: Event) =>
      sender ! (('SendEventLive, event, isEventLive(event)))
  }*/
}
