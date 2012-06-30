package com.livestream.events

object Main extends App {
  val eventServer = new EventServer(
    List[Event](),
    9999
  )

  eventServer.start
}
