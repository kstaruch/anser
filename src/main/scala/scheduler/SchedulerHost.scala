package scheduler

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.Http
import akka.stream.FlowMaterializer
import com.typesafe.config.ConfigFactory
import scheduler.api.{Core, SchedulerRoutes}


object SchedulerHost extends App with SchedulerRoutes with Core {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = FlowMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bind(interface = config.getString("http.interface"), port = config.getInt("http.port")).startHandlingWith(routes)
}
