package com.kst.routes.kafka

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.kst.models.ResultTopics
import com.kst.routes.Routes
import net.manub.embeddedkafka.EmbeddedKafka
import org.scalatest.{Ignore, Matchers, WordSpec}

@Ignore
class KafkaRouteSpec extends WordSpec with Routes with Matchers with ScalatestRouteTest with EmbeddedKafka {

  "check for response in list topics" should {
    val topic = "test_topic"
    "get topics in response" in {
      withRunningKafka {
        createCustomTopic(topic, Map("cleanup.policy" -> "compact"))
        Get("/kafka/topics") ~> kafkaApi ~> check {
          responseAs[ResultTopics].data.get.topics.headOption.get should be(topic)
        }
      }
    }
  }

  "publish funtionality" should {
    val topic = "test_topic"
    "get response for publish message in topic" in {
      withRunningKafka {
        createCustomTopic(topic, Map("cleanup.policy" -> "compact"))

        val requestEntity = HttpEntity(MediaTypes.`application/json`,
          s"""{"topic": "$topic", "schema": "LendingClub", "message":  "{\"name\": \"gerardo\", \"action\": \"vamos\"}"}""")

        Post("/kafka/topics") ~> kafkaApi ~> check {
          responseAs[ResultTopics].data.get.topics.headOption.get should be(topic)
        }
      }
    }
  }
}
