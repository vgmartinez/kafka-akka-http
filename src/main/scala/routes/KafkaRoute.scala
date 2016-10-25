package routes

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.server.Directives._
import spray.json._
import mappings.JsonMappings
import models.{DataAvroEntity, UserEntityUpdate}
import services.kafka.KafkaService

trait KafkaRoute extends JsonMappings with SecurityDirectives with KafkaService{
  val kafkaApi = pathPrefix("kafka") {
    pathPrefix("topics") {
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            complete{
              fetchTopics().map(_.toJson)
            }
          }
        }
      }~
      path(Rest) { topicName =>
        pathEndOrSingleSlash {
          authenticate { loggedUser =>
            get {
              complete(fetchTopicInfo(topicName).map(_.toJson))
            }
          }
        }
      }~
      path(Rest) { topicName =>
        pathEndOrSingleSlash {
          authenticate { loggedUser =>
            post {
              entity(as[String]) { dataForPublish =>
                complete(publishInTopicAsAvro(topicName, dataForPublish).map(_.toJson))
              }
            }
          }
        }
      }
    }
  }
}
