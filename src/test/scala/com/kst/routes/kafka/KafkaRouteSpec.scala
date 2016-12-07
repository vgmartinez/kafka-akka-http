package com.kst.routes.kafka

import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.kst.models.{ResultMetadata, ResultTopics}
import com.kst.routes.Routes
import net.manub.embeddedkafka.EmbeddedKafka
import org.scalatest.{Ignore, Matchers, WordSpec}

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
    val topic = "volcado"
    "get response for publish message in topic" in {
      withRunningKafka {
        createCustomTopic(topic, Map("cleanup.policy" -> "compact"))
        val request = "{\"topic\": \"volcado\",\"schemaName\": \"uniqueId\", \"message\": \"{\\\"id\\\": 1,\\\"loan_amnt\\\": 1,\\\"funded_amnt\\\": 2500.0," +
          "\\\"grade\\\": \\\"E\\\",\\\"emp_length\\\": \\\"10+ years\\\",\\\"home_ownership\\\": \\\"RENT\\\",\\\"annual_inc\\\": 1," +
          "\\\"loan_status\\\": \\\"Fully Paid\\\",\\\"purpose\\\": \\\"small_business\\\",\\\"zip_code\\\": \\\"606xx\\\",\\\"addr_state\\\": \\\"CA\\\"," +
          "\\\"total_pymnt_inv\\\": 12226.3}\"}"
        val requestEntity = HttpEntity(MediaTypes.`application/json`, request)
        Post("/kafka/topics", requestEntity) ~> kafkaApi ~> check {
          responseAs[ResultMetadata].data.get.topic should be(topic)
        }
      }
    }
  }
}
