package scheduler.api

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import akka.http.marshalling.ToResponseMarshallable
import akka.http.server.Route
import akka.http.server.Directives._
import scheduler.api.ApiMessages.Message
import akka.http.model.StatusCodes._
import akka.http.marshallers.sprayjson.SprayJsonSupport._

trait ServiceProvider extends SchedulerProtocols {
  this: Core =>
  implicit val services: Services
  implicit val logger: LoggingAdapter

  def withService(id: String)(action: ActorRef => Route) = {
    services.get(id.toLowerCase) match {
      case Some(provider) =>
        action(provider)

      case None =>
        logger.error(s"Unsupported service: $id")
        complete(ToResponseMarshallable(BadRequest -> Message(ApiMessages.UnsupportedService)))
    }
  }
}
