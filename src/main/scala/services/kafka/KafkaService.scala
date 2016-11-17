package services.kafka

import services.Base
import java.util.Properties
import models.{MessageEntity, MetadataResponse}
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

  System.setProperty("java.security.krb5.conf", "/etc/krb5.conf")
  System.setProperty("java.security.auth.login.config", "/home/victorgarcia/kafka-akka-http/src/main/resources/kafka_jaas.conf")
  System.setProperty("javax.security.auth.useSubjectCredsOnly", "false")
  System.setProperty("sun.security.krb5.debug", "true")

  def publishInTopic(message: MessageEntity): Future[MetadataResponse] = {
    val producer = new KafkaProducer[String, String](props)
    val topicName = message.topic
    val record = new ProducerRecord(topicName, "key", message.message)

    val producerResponse = producer.send(record).get
    producer.close()
    Future {
      MetadataResponse(producerResponse.topic, producerResponse.partition)
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
