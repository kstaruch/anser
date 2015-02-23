package scheduler.api

import akka.event.NoLogging
import akka.http.model.StatusCodes._
import akka.http.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, FlatSpec}
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import scheduler.api.ApiMessages.Message

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
}
