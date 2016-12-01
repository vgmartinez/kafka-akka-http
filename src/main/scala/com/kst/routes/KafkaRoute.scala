package com.kst.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import com.kst.mappings.JsonMappings
import com.kst.models.MessageEntity
import com.kst.services.kafkaService.KafkaService._
import com.kst.services.ranger.RangerService
import spray.json._

trait KafkaRoute extends JsonMappings with RangerService {
  val kafkaApi = pathPrefix("kafka") {
    pathPrefix("topics") {
      pathEndOrSingleSlash {
        //authenticate { loggedUser =>
          get {
            complete {
              getTopics.map(_.toJson)
            }
          }~
          post {
            entity(as[MessageEntity]) { messageForPublish =>
              authorize(isAuthorizeForPublish("kafka", messageForPublish.topic)) {
                complete(OK -> publish(messageForPublish).map(_.toJson))
              }
            }
          }
        //}
      }
    }
  }
}
