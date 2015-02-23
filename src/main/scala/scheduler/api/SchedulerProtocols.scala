package scheduler.api

import scheduler.api.ApiMessages.Message
import spray.json.DefaultJsonProtocol

trait SchedulerProtocols extends DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat1(Message.apply)
}
