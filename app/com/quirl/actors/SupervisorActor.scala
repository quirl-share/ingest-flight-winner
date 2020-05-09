package com.quirl.actors

import akka.actor.{Actor, ActorSystem}
import com.quirl.services.JsonFileStreamService
import com.quirl.utils.Start
import play.api.Logging

import scala.concurrent.{ExecutionContext, Future}

class SupervisorActor extends Actor with Logging {
  logger.info("Started SupervisorActor")
  implicit val system: ActorSystem = context.system
  implicit val cc: ExecutionContext = system.dispatcher

  self ! Start
  override def receive: Receive = {
    case Start => {
      logger.info("Started service")
      val service = new JsonFileStreamService()
      service.test.onComplete(
        f => logger.info("Size: " + f.get.size)
      )

    }
    case default => logger.error("Unexprected input: " + default)
  }
}
