package com.livestream.events

import java.net.URL

/**
 *  TODO: Not really an actor yet, but we'll deal with that later
**/
class EventActor {

  def isEventLive(event: Event): Boolean = {
    val url = new URL(event.url)
    false
  }
}
