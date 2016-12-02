package com.kst

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.kst.routes.Routes
import com.kst.utils.{Config, MigrationConfig}

object Main extends App with Config with MigrationConfig with Routes{
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  val log: LoggingAdapter = Logging(system, getClass)

  //migrate()

  Http().bindAndHandle(routes, interface = httpInterface, port = httpPort.toInt)
}
