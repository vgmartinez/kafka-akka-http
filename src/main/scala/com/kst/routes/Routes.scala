package com.kst.routes

import akka.http.scaladsl.server.Directives._
import com.kst.utils.CorsSupport
import akka.http.scaladsl.model.StatusCodes._

trait Routes extends ApiErrorHandler with KafkaRoute with CorsSupport {
  val routes =
    pathPrefix("v1") {
      kafkaApi
    } ~ path("healthCheck")(complete(OK))
}
