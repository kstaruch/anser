package scheduler.api

import scheduler.api.ApiMessages.Message
import scheduler.services.ApplicationsService.{GetAllApplicationsResponse, ApplicationInfo}
import scheduler.services.EventsService.{EventInfo, GetAllEventsResponse}
import spray.json.DefaultJsonProtocol

trait SchedulerProtocols extends DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat1(Message.apply)
  implicit val applicationInfoFormat = jsonFormat2(ApplicationInfo.apply)
  implicit val getAllApplicationsResponseFormat = jsonFormat1(GetAllApplicationsResponse.apply)
  implicit val eventInfoFormat = jsonFormat2(EventInfo.apply)
  implicit val getAllEventsResponseFormat = jsonFormat1(GetAllEventsResponse.apply)
}
