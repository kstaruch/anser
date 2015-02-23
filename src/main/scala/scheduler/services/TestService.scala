package scheduler.services

import akka.actor.{ActorLogging, Actor, Props}
import scheduler.services.TestService.Test

object TestService {
  class TestsServiceImpl extends TestService with IsAliveActor

  case object Test

  def props() = Props(classOf[TestsServiceImpl])
}

class TestService extends Actor with ActorLogging {
  override def receive: Receive = {
    case Test => ;

  }
}
