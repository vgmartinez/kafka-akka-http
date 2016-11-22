package routes

import akka.http.scaladsl.server.Directives._
import services.kafka.RangerService
import utils.CorsSupport

trait Routes extends ApiErrorHandler with AuthRoute with UsersRoute with KafkaRoute with CorsSupport with RangerService {
  val routes =
    pathPrefix("v1") {
      authRoute ~
      usersApi ~
      kafkaApi
    } ~ path("")(getFromResource("public/index.html"))
}
