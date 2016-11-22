package mappings

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models._
import spray.json.DefaultJsonProtocol

trait JsonMappings extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat5(UserEntity)
  implicit val tokenFormat = jsonFormat3(TokenEntity)
  implicit val loginFormat = jsonFormat2(LoginPassword)
  implicit val userEntityUpdateFormat = jsonFormat4(UserEntityUpdate)
  implicit val topicsFormat = jsonFormat1(Topics.apply)
  implicit val topicInfoFormat = jsonFormat3(TopicInfo.apply)
  implicit val messageFormat = jsonFormat2(MessageEntity.apply)
  implicit val metadataResponse = jsonFormat2(MetadataResponse)

  implicit val accessesEntity = jsonFormat2(Accesses)
  implicit val policyItemsEntity = jsonFormat5(PolicyItems)
  implicit val topicEntity = jsonFormat3(Topic)
  implicit val resourceEntity = jsonFormat1(Resource)
  implicit val policyEntity = jsonFormat14(PolicyEntity)

}