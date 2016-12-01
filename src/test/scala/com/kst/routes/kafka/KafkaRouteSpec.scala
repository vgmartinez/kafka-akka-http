package com.kst.routes.kafka

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.kst.models.ResultTopics
import com.kst.routes.Routes
import com.kst.services.kafkaService.KafkaService._
import net.manub.embeddedkafka.EmbeddedKafka
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class KafkaRouteSpec extends WordSpec with ScalaFutures with Matchers with ScalatestRouteTest with Routes with EmbeddedKafka {

  "Kafka service" should {
    "retrieve users list" in {
      withRunningKafka {
        Get("/kafka/topics") ~> kafkaApi ~> check {
          responseAs[ResultTopics].data.get.topics.isEmpty should be(true)
        }
      }
    }
  }
}
