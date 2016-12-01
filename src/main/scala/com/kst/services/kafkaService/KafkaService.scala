package com.kst.services.kafkaService

import java.io.ByteArrayOutputStream

import com.kst.services.Base
import java.util.Properties

import com.kst.models._
import com.sksamuel.avro4s.{AvroInputStream, AvroOutputStream}
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream
import net.manub.embeddedkafka.KafkaUnavailableException
import org.apache.avro.AvroTypeException
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
  props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
  props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
  props.put("security.protocol", s"$kafkaSecurityProtocol")
  props.put("sasl.kerberos.service.name", "kafka")

  val producer = new KafkaProducer[String, Array[Byte]](props)
  val consumer = new KafkaConsumer[String, String](props)

  def serializeInAvro(schemaName: String, message: String): Array[Byte] = {
    try {
      val in = new ByteInputStream(message.getBytes("UTF-8"), message.length)

      val input = AvroInputStream.json[LendingClub](in)
      val lendingClub = input.singleEntity.get

      val output = new ByteArrayOutputStream
      val avro = AvroOutputStream.data[LendingClub](output)
      avro.write(lendingClub)
      avro.close()
      output.toByteArray
    } catch {
      case e: Exception => {
        throw new AvroTypeException(e.getMessage)
      }
    }
  }

  def publish(entityMessage: MessageEntity): Future[Option[ResultMetadata]] = {
    val topicName = entityMessage.topic
    val message = serializeInAvro(entityMessage.schemaName, entityMessage.message)
    val response = publishInTopic(topicName, message)
    closeProducer()
    response
  }

  def publishInTopic(topicName: String, message: Array[Byte]): Future[Option[ResultMetadata]] = {
    val record = new ProducerRecord(topicName, "key", message)
    try {
      val producerResponse = producer.send(record).get
      val metadata = Metadata(producerResponse.timestamp, producerResponse.topic, producerResponse.partition)
      Future.successful {
        Some(ResultMetadata(200, "COGNOS200", "Successful publish in topic", Some(metadata)))
      }
    } catch {
      case e: Exception => {
        Future.successful {
          Some(ResultMetadata(500, "COGNOS500", "Internal error server"))
        }
        throw new KafkaUnavailableException(e)
      }
    }
  }

  def getTopics: Future[Option[ResultTopics]] = {
    val topics = fetchTopics
    closeConsumer()
    topics
  }

  def fetchTopics: Future[Option[ResultTopics]] = {
    try {
      val topics = consumer.listTopics()
      Future {
        val seqOfTopics: Seq[String] = for { topicName <- topics.keySet().toArray() } yield topicName.toString
        val topicsNames = Topics(System.currentTimeMillis(), seqOfTopics.toList)
        Some(ResultTopics(200, "COGNOS200", "Successful retrieve all topics" , Some(topicsNames)))
      }
    } catch {
      case e: Exception => {
        Future.successful {
          Some(ResultTopics(500, "COGNOS500", "Internal error server"))
        }
        throw new KafkaUnavailableException(e)
      }
    }
  }

  def closeProducer(): Unit = producer.close()

  def closeConsumer(): Unit = consumer.close()
}
