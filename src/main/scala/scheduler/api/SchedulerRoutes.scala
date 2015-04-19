package scheduler.api

import akka.actor.ActorRef
import akka.event.LoggingAdapter
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.model.HttpHeader
import akka.http.model.StatusCodes._
import akka.http.server.Directives._
import akka.http.server.Route
import akka.http.server.directives.BasicDirectives
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config
import scheduler.api.ApiMessages.Message
import scheduler.services.ApplicationsService
import scheduler.services.ApplicationsService._
import scheduler.services.EventsService.{GetAllEvents, GetAllEventsResponse}
import scheduler.services.IsAliveActor.IsAlive

import scala.concurrent.duration._
import scala.util.{Failure, Success}
import akka.http.model.headers._
import akka.http.model._

/*
Scheduler api
  /scheduler/applications/{applicationId}/events/{eventId}
  /scheduler/applications/{applicationId}/queries/
  /scheduler/applications/{applicationId}/tests/

 */

trait SchedulerRoutes extends SchedulerProtocols with SchedulerActors with ServiceProvider with ResponseFactory {
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
        path("applications") {
          (post & entity(as[ApplicationInfo]) & extractUri) {
            (applicationInfo, uri) => respondWithHeader(Location(uri.withPath(Uri.Path("/applications/" + applicationInfo.id )))) {
              withService("applications") { service => handleAddApplication(service, applicationInfo) }
            }
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

  def handleAddApplication(service: ActorRef, info: ApplicationInfo): Route = {
    createCreatedResponse((service ? AddApplication(info)).mapTo[String])
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

}
