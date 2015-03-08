package scheduler.api

import scheduler.services.{EventsService, ApplicationsService, TestService}

trait SchedulerActors extends SchedulerProtocols {
  this: Core =>

//  lazy val schedulerService = system.actorOf(SchedulerService.props(), "SchedulerService")
  lazy val eventsService = system.actorOf(EventsService.props(), "EventsService")
  lazy val applicationsService = system.actorOf(ApplicationsService.props(), "ApplicationsService")
//  lazy val queriesService = system.actorOf(QueriesService.props(), "QueriesService")
  lazy val testService = system.actorOf(TestService.props(), "TestService")

  lazy val services: Services = Map(
//    "scheduler" -> schedulerService,
    "events" -> eventsService,
    "applications" -> applicationsService,
//    "queries" -> queriesService,
    "test" -> testService
  )
}
