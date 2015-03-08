package scheduler.services

import akka.actor.{Props, Actor, ActorLogging}
import scheduler.services.ApplicationsService.ApplicationId
import scheduler.services.EventsService.{GetAllEvents, EventInfo, GetAllEventsResponse}

object EventsService {

  class EventsServiceImpl extends EventsService with IsAliveActor

  def props() = Props(classOf[EventsServiceImpl])

  case class GetAllEvents(applicationId: ApplicationId)
  case class GetAllEventsResponse(events: Seq[EventInfo])
  case class EventInfo(applicationId: ApplicationId, info: String)
}

class EventsService extends Actor with ActorLogging{
  override def receive: Receive = {
    case GetAllEvents(applicationId) => sender ! GetAllEventsResponse(
      EventInfo(applicationId, "some event info") :: EventInfo(applicationId, "some event info") :: Nil
    );
  }
}
