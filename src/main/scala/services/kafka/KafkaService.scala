package services.kafka

import mappings.JsonMappings
import services.Base
import java.util.Properties
import models.{MessageEntity, MetadataResponse}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object KafkaService extends Base with JsonMappings {
  val props = new Properties()

  props.put("bootstrap.servers", "sandbox.hortonworks.com:6667")
  props.put("acks", "all")
  props.put("retries", "0")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("security.protocol", "SASL_PLAINTEXT")
  props.put("sasl.kerberos.service.name", "kafka")


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

  def fetchTopics(): Future[Unit] = Future.successful()

  def fetchTopicInfo(topicName: String): Future[Unit] = Future.successful()
}
