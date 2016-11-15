package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import mappings.JsonMappings
import models.MessageEntity
import services.kafka.KafkaService._
import scala.concurrent.ExecutionContext.Implicits.global
import spray.json._

trait KafkaRoute extends JsonMappings with SecurityDirectives {
  val kafkaApi = pathPrefix("kafka") {
    pathPrefix("topics") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            complete {
              fetchTopics.map(_.toJson)
            }
          }
          post {
            entity(as[MessageEntity]) { messageForPublish =>
              complete(OK -> publishInTopic(messageForPublish).map(_.toJson))
            }
          }
        }
      }
    }
  }
}
