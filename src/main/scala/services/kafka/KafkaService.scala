package services.kafka

import mappings.JsonMappings
import services.Base
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.Properties
import scala.concurrent.ExecutionContext.Implicits.global
import models.MessageEntity
import scala.concurrent.Future

object KafkaService extends Base with JsonMappings {
  val props = new Properties()

  props.put("bootstrap.servers", "sandbox.hortonworks.com:6667")
  props.put("acks", "all")
  props.put("retries", "0")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("security.protocol", "SASL_PLAINTEXT")
  props.put("sasl.kerberos.service.name", "kafka")


  def publishInTopic(message: MessageEntity): Future[String] = {
    val producer = new KafkaProducer[String, String](props)
    val topicName = message.topic
    val record = new ProducerRecord(topicName, "key", message.message)

    val f: Future[Unit] = Future {
      producer.send(record)
      producer.close()
    }

    f recoverWith {
      case ex:Exception => Future.failed(new Exception(ex))
    }
    f.mapTo[String]
  }

  def fetchTopics(): Future[Unit] = Future.successful()

  def fetchTopicInfo(topicName: String): Future[Unit] = Future.successful()
}
