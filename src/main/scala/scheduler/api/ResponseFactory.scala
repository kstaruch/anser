package scheduler.api

import akka.event.LoggingAdapter
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.ToResponseMarshallable
import akka.http.model.StatusCodes._
import akka.http.server.Directives._
import akka.http.server.Route
import scheduler.api.ApiMessages.Message

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait ResponseFactory extends SchedulerProtocols {
  this: Core =>

  implicit val logger: LoggingAdapter
                                                                                1
  def createResponse[T](eventualResponse: Future[T])(implicit marshaller: T => ToResponseMarshallable): Route = {
    onComplete(eventualResponse) {
      case Success(result) =>
        complete(result)
      case Failure(e) =>
        logger.error(s"Error: ${e.toString}")
        complete(ToResponseMarshallable(InternalServerError -> Message(ApiMessages.UnknownException)))
    }
  }

  def createCreatedResponse[T](eventualResponse: Future[T])(implicit marshaller: T => ToResponseMarshallable): Route = {
    onComplete(eventualResponse) {
      case Success(result) =>
        complete(ToResponseMarshallable(Created -> Message(result.toString)))
      case Failure(e) =>
        logger.error(s"Error: ${e.toString}")
        complete(ToResponseMarshallable(InternalServerError -> Message(ApiMessages.UnknownException)))
    }
  }
}
