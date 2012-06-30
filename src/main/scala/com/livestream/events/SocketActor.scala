package com.livestream.events

import akka.actor.{Actor, ActorRef, PoisonPill}

import java.io.{BufferedReader,InputStreamReader,PrintWriter}

import java.net.Socket

/*
 *  Wrapper actor for a single socket.  Will handle new commands from the
 *  client and either forward them to the event actor for further
 *  processing or handle the command itself.
 */

class SocketActor(
  sock: Socket,
  sockIn: BufferedReader,
  sockOut: PrintWriter,
  eventActor: ActorRef,
  events: Map[String, Event]
) extends Actor {

  handleCommand(sockIn.readLine)

  def handleCommand(command: String) {
    command match {
      case Commands.PING =>
        //pong and then wait for client
        sockOut.println(Commands.PONG)
        sockOut.flush
        self ! 'WaitForRead
      case Commands.GET_LIVESTATUS =>
        //read event name
        val eventName = sockIn.readLine
        if(events.contains(eventName)) {
          //get event status, event actor will let us know
          //when it's ready
          eventActor ! ('GetEventLive, events(eventName))
        } else {
          //handle noevent error and wait for client
          sockOut.println(Commands.ERROR)
          sockOut.println("EventDNE")
          sockOut.flush
          self ! 'WaitForRead
        }
      //Any other command will close the connection*/
      case _ =>
        self ! PoisonPill
    }
  }

  def receive = {
    case ('SendEventLive, isLive: Boolean) =>
      sockOut.println(Commands.SEND_LIVESTATUS)
      sockOut.println(isLive)
      sockOut.flush

      handleCommand(sockIn.readLine)
    case 'WaitForRead =>
      handleCommand(sockIn.readLine)
  }

  override def postStop {
    //cleanup when actor dies
    try { sockIn.close } catch { case ex: Exception => ex.printStackTrace }
    try { sockOut.close } catch { case ex: Exception => ex.printStackTrace }
    try { sock.close } catch { case ex: Exception => ex.printStackTrace }
  }
}
