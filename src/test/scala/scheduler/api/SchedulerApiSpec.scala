package scheduler.api

import akka.event.NoLogging
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.model.HttpHeader
import akka.http.model.StatusCodes._
import akka.http.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}
import scheduler.api.ApiMessages.Message
import scheduler.services.ApplicationsService.{ApplicationInfo, GetAllApplicationsResponse}
import scheduler.services.EventsService.GetAllEventsResponse

class SchedulerApiSpec extends FlatSpec with Matchers with ScalatestRouteTest with SchedulerRoutes with Core with SchedulerProtocols {
  //def actorRefFactory = system
  override def testConfigSource = "akka.loglevel = WARNING"

  override def config = testConfig

  override val logger = NoLogging


  behavior of "Scheduler API"

  it should "complete 'scheduler/api/isalive' endpoint" in {
    Get(s"/scheduler/api/isalive") ~> routes ~> check {
      status should be (OK)
      responseAs[String] should be ("Alive")
    }
  }

  it should "complete 'scheduler/api/test/isalive' endpoint" in {
    Get(s"/scheduler/api/test/isalive") ~> routes ~> check {
      status should be (OK)
      responseAs[String] should be ("Alive")
    }
  }

  it should "complete 'scheduler/api/not_existing_service/isalive' with BadRequest" in {
    Get(s"/scheduler/api/not_existing_service/isalive") ~> routes ~> check {
      status should be (BadRequest)
      responseAs[Message].message should include (ApiMessages.UnsupportedService)
    }
  }

  it should "GET 'scheduler/api/applications' endpoint with list of applications" in {
    Get(s"/scheduler/api/applications") ~> routes ~> check {
      status should be (OK)
      responseAs[GetAllApplicationsResponse].applications should have length 2
    }
  }

  it should "complete 'scheduler/applications/XXX/events/isalive' endpoint" in {
        Get(s"/scheduler/api/applications/XXX/events/isalive") ~> routes ~> check {
          status should be (OK)
          responseAs[String] should be ("Alive")
        }
  }

  it should "GET 'scheduler/api/applications/XXX/events' endpoint with list of events for XXX" in {
      Get(s"/scheduler/api/applications/XXX/events") ~> routes ~> check {
        status should be (OK)
        responseAs[GetAllEventsResponse].events.filter(e => e.applicationId == "XXX") should have length 2
      }
  }

  it should "complete POST to 'scheduler/api/applications/' with 201 and location when valid data provided" in {
    Post(s"/scheduler/api/applications", ApplicationInfo("XXX", "XXX description")) ~> routes ~> check {
      status should be (Created)
      val loc = header("location")
      loc should be (defined)
    }
  }
}
