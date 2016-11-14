package routes

import akka.http.scaladsl.server.Directives._
import mappings.JsonMappings
import models.MessageEntity
import services.kafka.KafkaService._

trait KafkaRoute extends JsonMappings with SecurityDirectives {
  val kafkaApi = pathPrefix("kafka") {
    pathPrefix("topics") {
      /*pathEndOrSingleSlash {
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
      }~*/
      path(Rest) { topicName =>
        pathEndOrSingleSlash {
          authenticate { loggedUser =>
            post {
              entity(as[MessageEntity]) { messageForPublish =>
                complete(publishInTopic(messageForPublish))
              }
            }
          }
        }
      }
    }
  }
}
