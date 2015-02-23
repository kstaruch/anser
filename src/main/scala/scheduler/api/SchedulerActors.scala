package scheduler.api

import scheduler.services.TestService

trait SchedulerActors extends SchedulerProtocols {
  this: Core =>



  //implicit val logger: LoggingAdapter

//  lazy val schedulerService = system.actorOf(SchedulerService.props(), "SchedulerService")
//  lazy val eventsService = system.actorOf(EventsService.props(), "EventsService")
//  lazy val applicationsService = system.actorOf(ApplicationsService.props(), "ApplicationsService")
//  lazy val queriesService = system.actorOf(QueriesService.props(), "QueriesService")
  lazy val testService = system.actorOf(TestService.props(), "TestService")

  lazy val services: Services = Map(
//    "scheduler" -> schedulerService,
//    "events" -> eventsService,
//    "applications" -> applicationsService,
//    "queries" -> queriesService,
    "test" -> testService
  )

//  def withService(id: String)(action: ActorRef => Route) = {
//    services.get(id.toLowerCase) match {
//      case Some(provider) =>
//        action(provider)
//
//      case None =>
//        logger.error(s"Unsupported service: $id")
//        complete(BadRequest -> Message(ApiMessages.UnsupportedService))
//    }
//  }
}
