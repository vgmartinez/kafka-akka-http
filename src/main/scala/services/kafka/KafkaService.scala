
package services.kafka

import services.Base
import java.util.Properties

import models.{MessageEntity, Metadata, Result}
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object KafkaService extends Base {
  val props = new Properties()
  props.put("bootstrap.servers", s"$kafkaHost:$kafkaPort")
  props.put("acks", "all")
  props.put("retries", "0")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
  props.put("security.protocol", "SASL_PLAINTEXT")
  props.put("sasl.kerberos.service.name", "kafka")

  val producer = new KafkaProducer[String, String](props)

  def publishInTopic(message: MessageEntity): Future[Option[Result]] = {
    val topicName = message.topic
    val record = new ProducerRecord(topicName, "key", message.message)
    try {
      val producerResponse = producer.send(record).get
      val metadata = Metadata(producerResponse.timestamp, producerResponse.topic, producerResponse.partition)
      Future.successful {
        Some(Result(200, "COGNOS200", "Successful publish in topic", Some(metadata)))
      }
    } catch {
      case e: Exception => {
        println(e.getMessage)
        Future.successful {
          Some(Result(500, "COGNOS500", "Internal error server"))
        }
      }
    } finally {
      producer.close()
    }
  }

  def fetchTopics: Future[Seq[String]] = {
    val consumer = new KafkaConsumer[String, String](props)
    val topics = consumer.listTopics()
    Future {
      val seqOfTopics: Seq[String] = for { topicName <- topics.keySet().toArray() } yield topicName.toString
      seqOfTopics
    }
  }
}
