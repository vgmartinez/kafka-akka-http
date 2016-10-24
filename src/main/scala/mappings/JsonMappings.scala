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
}