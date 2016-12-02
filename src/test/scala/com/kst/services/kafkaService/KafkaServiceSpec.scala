package com.kst.services.kafkaService

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.kst.models.{MessageEntity, ResultMetadata, ResultTopics}
import com.kst.services.kafkaService.KafkaService._
import kafka.admin.AdminUtils
import kafka.utils.ZkUtils
import net.manub.embeddedkafka.{EmbeddedKafka, EmbeddedKafkaConfig, KafkaUnavailableException}
import org.apache.avro.AvroTypeException
import org.apache.kafka.common.serialization.BytesSerializer
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by victorgarcia on 29/11/16.
  */
class KafkaServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with EmbeddedKafka {
  val lendingClub =
      """
        |{
        |	"id": 1,
        |	"loan_amnt": 1,
        |	"funded_amnt": 2500.0,
        |	"grade": "E",
        |	"emp_length": "10+ years",
        |	"home_ownership": "RENT",
        |	"annual_inc": 1,
        |	"loan_status": "Fully Paid",
        |	"purpose": "small_business",
        |	"zip_code": "606xx",
        |	"addr_state": "CA",
        |	"total_pymnt_inv": 12226.3
        |}
      """.stripMargin

  val lendingClubBad =
      """
        |{
        |	"id": 1,
        |	"loan_amnt": "1",
        |	"funded_amnt": 2500.0,
        |	"grade": "E",
        |	"emp_length": "10+ years",
        |	"home_ownership": "RENT",
        |	"annual_inc": 1,
        |	"loan_status": "Fully Paid",
        |	"purpose": "small_business",
        |	"zip_code": "606xx",
        |	"addr_state": "CA",
        |	"total_pymnt_inv": 12226.3
        |}
      """.stripMargin

  override def afterAll(): Unit = {
    closeProducer()
    closeConsumer()
    system.terminate()
  }

  "the publishStringMessageToKafka method" should {
    val topic = "test_topic"
    val messageToPublish = MessageEntity(topic, "LendingClub", lendingClub)
    var response: Future[Option[ResultMetadata]] = null
    "publish event in topic" in {
      withRunningKafka {
        response = publish(messageToPublish)
      }
    }

    "publish message with bad schema format" in {
      withRunningKafka {
        a[AvroTypeException] shouldBe thrownBy {
          implicit val serializer = new BytesSerializer
          val messageToPublish = MessageEntity(topic, "LendingClub", lendingClubBad)
          publish(messageToPublish)
        }
      }
    }

    "retrieve status code 200" in {
      withRunningKafka {
        var currentResult: ResultMetadata = null
        val currentMetadata = response map { messageToPublish =>
          currentResult = messageToPublish.get
        }

        Await.result(currentMetadata, 20.second)
        assert(currentResult.code == "COGNOS200")
      }
    }

    "check the topic name" in {
      withRunningKafka {
        var currentResult: ResultMetadata = null
        val currentMetadata = response map { messageToPublish =>
          currentResult = messageToPublish.get
        }

        Await.result(currentMetadata, 20.second)
        assert(currentResult.data.get.topic == topic)
      }
    }

    "throw a KafkaUnavailableException when Kafka is unavailable when trying to publish" in {
      a[KafkaUnavailableException] shouldBe thrownBy {
        implicit val serializer = new BytesSerializer
        val messageToPublish = MessageEntity(topic, "LendingClub", lendingClub)
        publish(messageToPublish)
      }
    }
  }

  "the createCustomTopic method" should {
    implicit val config = EmbeddedKafkaConfig(customBrokerProperties = Map("log.cleaner.dedupe.buffer.size" -> "2000000"))
    val topic = "test_custom_topic"
    "create a topic with a custom configuration" in {
      withRunningKafka {

        createCustomTopic(topic, Map("cleanup.policy" -> "compact"))

        val zkSessionTimeoutMs = 10000
        val zkConnectionTimeoutMs = 10000
        val zkSecurityEnabled = false

        val zkUtils = ZkUtils(s"localhost:${config.zooKeeperPort}", zkSessionTimeoutMs, zkConnectionTimeoutMs, zkSecurityEnabled)
        try {
          AdminUtils.topicExists(zkUtils, topic) shouldBe true
        } finally zkUtils.close()
      }
    }
  }

  "check for topics" should {
    val topic = "test_custom_topic"

    "list topics success code" in {
      withRunningKafka {
        createCustomTopic(topic, Map("cleanup.policy" -> "compact"))
        val topics = fetchTopics
        var currentTopic: ResultTopics = null
        val currentBlock = topics map { resultTopic =>
          currentTopic = resultTopic.get
        }

        Await.result(currentBlock, 20.second)

        assert(currentTopic.code == "COGNOS200")
        assert(currentTopic.data.get.topics.head == topic)
        assert(currentTopic.data.get.timestamp.isInstanceOf[Long])
      }
    }
  }
}

