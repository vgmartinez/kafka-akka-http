package services.kafka

import mappings.JsonMappings
import services.Base
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Date
import java.util.Properties
import java.util.Random

import scala.concurrent.Future

object KafkaService extends Base with JsonMappings {
  val props = new Properties()

  props.put("bootstrap.servers", "sandbox.hortonworks.com:6667")
  props.put("acks", "all")
  props.put("retries", "0")
  props.put("batch.size", "16384")
  props.put("linger.ms", "1")
  props.put("buffer.memory", "33554432")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("retry.backoff.ms", "500")
  props.put("security.protocol", "SASL_PLAINTEXT")
  props.put("sasl.kerberos.service.name", "kafka")


  //props.put("listeners", "PLAINTEXT://localhost:6667")
  //props.put("security.protocol", "PLAINTEXT")

  def publish(message: String): Future[Unit] = {
    println("-------------------------------------")
    println(message)
    val producer = new KafkaProducer[String, String](props)

    val TOPIC="uniqueId"

    for(i<- 1 to 50){
      val record = new ProducerRecord(TOPIC, "key", s"hello $i")
      producer.send(record)
    }

    val record = new ProducerRecord(TOPIC, "key", "the end "+new java.util.Date)
    producer.send(record)

    producer.close()
    Future.successful()
  }

  def fetchTopics(): Future[Unit] = Future.successful()

  def fetchTopicInfo(topicName: String): Future[Unit] = Future.successful()
}
