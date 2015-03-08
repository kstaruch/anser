package scheduler.api

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.model.StatusCodes._
import akka.http.server.Directives._
import akka.http.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import scheduler.api.ApiMessages.Message
import scheduler.services.ApplicationsService.{GetAllApplicationsResponse, GetAll}
import scheduler.services.IsAliveActor.IsAlive

import scala.concurrent.duration._
import scala.util.{Failure, Success}

trait SchedulerRoutes extends SchedulerProtocols with SchedulerActors with ServiceProvider {
  this: Core =>

  def config: Config
  implicit val logger: LoggingAdapter
  implicit val timeout = Timeout(10.seconds)

  val routes = {
    logRequestResult("scheduler-service") {
      pathPrefix("scheduler" / "api") {
        (get & path("isalive")) {
          complete("Alive")
        } ~
        path(Segment / "isalive") {
          (serviceId) =>
            get {
              withService(serviceId) { service => handleIsAlive(service)}
            }
        } ~
        path("applications" / Segment / Segment / "isalive") {
          (applicationId, serviceId) =>
            get {
              withService(serviceId) { service => handleIsAlive(service)}
            }
        } ~
        path(Segment) {
          (serviceId) =>
            get {
              withService(serviceId) { service => handleGetAll(service) }
            }
        }
      }
    }
  }

  def handleIsAlive(service: ActorRef): Route = {
    val future = (service ? IsAlive).mapTo[String]

    onComplete(future) {
      case Success(result) =>
        complete(result)
      case Failure(e) =>
        logger.error(s"Error: ${e.toString}")
        complete(InternalServerError -> Message(ApiMessages.UnknownException))
    }
  }

  def handleGetAll(service: ActorRef): Route = {
    val future = (service ? GetAll).mapTo[GetAllApplicationsResponse]

    onComplete(future) {
      case Success(result) =>
        complete(result)
      case Failure(e) =>
        logger.error(s"Error: ${e.toString}")
        complete(InternalServerError -> Message(ApiMessages.UnknownException))
    }
  }
}
