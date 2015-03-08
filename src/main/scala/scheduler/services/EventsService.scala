package scheduler.services

import akka.actor.{Props, Actor, ActorLogging}
import scheduler.services.ApplicationsService.ApplicationId
import scheduler.services.EventsService.{EventInfo, GetAllEventsResponse, GetAll}

object EventsService {

  class EventsServiceImpl extends EventsService with IsAliveActor

  def props() = Props(classOf[EventsServiceImpl])

  case object GetAll
  case class GetAllEventsResponse(events: Seq[EventInfo])
  case class EventInfo(applicationId: ApplicationId, info: String)
}

class EventsService extends Actor with ActorLogging{
  override def receive: Receive = {
    case GetAll => sender ! GetAllEventsResponse(
      EventInfo("application 1", "some event info") :: EventInfo("application 2", "some event info") :: Nil
    );
  }
}
