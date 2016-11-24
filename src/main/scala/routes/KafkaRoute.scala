package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import mappings.JsonMappings
import models.MessageEntity
import services.kafka.KafkaService._
import services.ranger.RangerService
import spray.json._

trait KafkaRoute extends JsonMappings with SecurityDirectives with RangerService {
  val kafkaApi = pathPrefix("kafka") {
    pathPrefix("topics") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            complete {
              fetchTopics.map(_.toJson)
            }
          }~
          post {
            entity(as[MessageEntity]) { messageForPublish =>
              authorize(isAuthorizeForPublish(loggedUser.username, messageForPublish.topic)) {
                complete(OK -> publishInTopic(messageForPublish).map(_.toJson))
              }
            }
          }
        }
      }
    }
  }
}
