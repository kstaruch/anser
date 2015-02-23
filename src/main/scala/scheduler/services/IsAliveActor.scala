package scheduler.services

import akka.actor.Actor
import scheduler.services.IsAliveActor.IsAlive

object IsAliveActor {
  case object IsAlive
}

trait IsAliveActor extends Actor {
  abstract override def receive =
    super.receive orElse {
      //case IsAlive => sender ! "Alive"
      case IsAlive => sender ! "Alive"
    }
}
