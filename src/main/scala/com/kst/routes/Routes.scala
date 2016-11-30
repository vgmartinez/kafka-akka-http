package com.kst.routes

import akka.http.scaladsl.server.Directives._
import com.kst.utils.CorsSupport

trait Routes extends ApiErrorHandler with AuthRoute with UsersRoute with KafkaRoute with CorsSupport {
  val routes =
    pathPrefix("v1") {
      authRoute ~
      usersApi ~
      kafkaApi
    } ~ path("")(getFromResource("public/index.html"))
}
