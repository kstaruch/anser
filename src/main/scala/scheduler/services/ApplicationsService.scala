package scheduler.services

import akka.actor.{Actor, ActorLogging, Props}
import scheduler.services.ApplicationsService.{ApplicationInfo, GetAll, GetAllApplicationsResponse}

object ApplicationsService {
  type ApplicationId = String

  class ApplicationsServiceImpl extends ApplicationsService with IsAliveActor

  def props() = Props(classOf[ApplicationsServiceImpl])

  case class GetById(id: ApplicationId)

  case object GetAll

  case class GetAllApplicationsResponse(applications: Seq[ApplicationInfo])

  case class ApplicationInfo(id: ApplicationId, description: String)
}

class ApplicationsService extends Actor with ActorLogging {
  override def receive: Receive = {
    case GetAll => sender ! GetAllApplicationsResponse(
      ApplicationInfo("application1", "application 1 description")
        :: ApplicationInfo("application2", "application 2 description") :: Nil);
  }
}
