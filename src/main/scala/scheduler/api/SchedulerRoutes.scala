package scheduler.api

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.model.StatusCodes._
import akka.http.server.Directives._
import akka.http.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import scheduler.api.ApiMessages.Message
import scheduler.services.ApplicationsService.{ApplicationId, GetAllApplicationsResponse, GetAll}
import scheduler.services.EventsService.{GetAllEventsResponse, GetAllEvents}
import scheduler.services.IsAliveActor.IsAlive

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/*
Scheduler api
  /scheduler/applications/{applicationId}/events/{eventId}
  /scheduler/applications/{applicationId}/queries/
  /scheduler/applications/{applicationId}/tests/

 */

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
        path("applications" / Segment / "events") {
          (applicationId) =>
            get {
              withService("events") { service => handleGetAllEvents(service, applicationId)}
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
    createResponse((service ? IsAlive).mapTo[String])
  }

  def handleGetAll(service: ActorRef): Route = {
    createResponse((service ? GetAll).mapTo[GetAllApplicationsResponse])
  }

  def handleGetAllEvents(service: ActorRef, applicationId: ApplicationId): Route = {
    createResponse((service ? GetAllEvents(applicationId)).mapTo[GetAllEventsResponse])
  }

  def createResponse[T](eventualResponse: Future[T])(implicit marshaller: T => ToResponseMarshallable): Route = {
    onComplete(eventualResponse) {
      case Success(result) =>
        complete(result)
      case Failure(e) =>
        logger.error(s"Error: ${e.toString}")
        complete(InternalServerError -> Message(ApiMessages.UnknownException))
    }
  }


}
