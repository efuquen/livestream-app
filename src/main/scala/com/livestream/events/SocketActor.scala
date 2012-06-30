package com.livestream.events

import akka.actor.{Actor, ActorRef, PoisonPill}

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

import java.net.Socket

class SocketActor(
  sock: Socket,
  sockIn: BufferedReader,
  sockOut: PrintWriter,
  eventActor: ActorRef 
) extends Actor {

  handleCommand(sockIn.readLine)

  def handleCommand(command: String) {
    command match {
      case Commands.PING =>
        sockOut.println(Commands.PONG)
        sockOut.flush
        self ! 'WaitForRead
      /*case Commands.GET_LIVESTATUS =>
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
      //Any other command will close the connection*/
      case _ =>
        self ! PoisonPill
    }
  }

  def receive = {
    case 'WaitForRead =>
      handleCommand(sockIn.readLine)
  }

  override def postStop {
    try { sockIn.close } catch { case ex: Exception => ex.printStackTrace }
    try { sockOut.close } catch { case ex: Exception => ex.printStackTrace }
    try { sock.close } catch { case ex: Exception => ex.printStackTrace }
  }
}
