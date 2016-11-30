package com.kst.mappings

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.kst.models._
import models._
import spray.json.DefaultJsonProtocol

trait JsonMappings extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat5(UserEntity)
  implicit val tokenFormat = jsonFormat3(TokenEntity)
  implicit val loginFormat = jsonFormat2(LoginPassword)
  implicit val userEntityUpdateFormat = jsonFormat4(UserEntityUpdate)
  implicit val messageFormat = jsonFormat3(MessageEntity.apply)
  implicit val metadataResponse = jsonFormat3(Metadata)
  implicit val topicsResponse = jsonFormat2(Topics)
  implicit val resulMetadatatResponse = jsonFormat4(ResultMetadata)
  implicit val resultTopicsResponse = jsonFormat4(ResultTopics)

  implicit val accessesEntity = jsonFormat2(Accesses)
  implicit val policyItemsEntity = jsonFormat5(PolicyItems)
  implicit val topicEntity = jsonFormat3(Topic)
  implicit val resourceEntity = jsonFormat1(Resource)
  implicit val policyEntity = jsonFormat14(PolicyEntity)
}
