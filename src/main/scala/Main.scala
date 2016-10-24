import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import routes.Routes
import utils.{Config, MigrationConfig}

import scala.concurrent.ExecutionContext

object Main extends App with Config with MigrationConfig with Routes{
  implicit val system = ActorSystem()
  implicit val executor: ExecutionContext = system.dispatcher
  val log: LoggingAdapter = Logging(system, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  migrate()

  Http().bindAndHandle(routes, interface = httpInterface, port = httpPort)
}