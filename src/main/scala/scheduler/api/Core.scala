package scheduler.api

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer

import scala.concurrent.ExecutionContextExecutor

trait Core {
  implicit def system: ActorSystem
  implicit val materializer: FlowMaterializer
  implicit def executor: ExecutionContextExecutor
}
