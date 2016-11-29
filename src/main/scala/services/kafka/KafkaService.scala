
package services.kafka

import services.Base
import java.util.Properties

import models.{MessageEntity, Metadata, Result, Topics}
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
  props.put("security.protocol", "PLAINTEXTSASL")
  props.put("sasl.kerberos.service.name", "kafka")

  val producer = new KafkaProducer[String, String](props)
  val consumer = new KafkaConsumer[String, String](props)

  def publishInTopic(message: MessageEntity): Future[Option[Result]] = {
    val topicName = message.topic
    val record = new ProducerRecord(topicName, "key", message.message)
    try {
      val producerResponse = producer.send(record).get
      producer.close()
      val metadata = Metadata(producerResponse.timestamp, producerResponse.topic, producerResponse.partition)
      Future.successful {
        Some(Result(200, "COGNOS200", "Successful publish in topic", Some(metadata)))
      }
    } catch {
      case e: Exception => {
        Future.successful {
          Some(Result(500, "COGNOS500", "Internal error server"))
        }
      }
    }
  }

  def fetchTopics: Future[Option[Result]] = {
    try {
      val topics = consumer.listTopics()
      consumer.close()
      Future {
        val seqOfTopics: Seq[String] = for { topicName <- topics.keySet().toArray() } yield topicName.toString
        val topicsNames = Topics(System.currentTimeMillis(), seqOfTopics.toList)
        Some(Result(200, "COGNOS200", "Successful retrieve all topics" , Some(topicsNames)))
      }
    } catch {
      case e: Exception => {
        Future.successful {
          Some(Result(500, "COGNOS500", "Internal error server"))
        }
      }
    }

  }
}
