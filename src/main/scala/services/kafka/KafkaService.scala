package services.kafka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import java.io.IOException
import akka.util.ByteString
import mappings.JsonMappings
import services.Base

import scala.concurrent.{ExecutionContext, Future}

trait KafkaService extends Base with JsonMappings {
  implicit val system: ActorSystem
  implicit val executor: ExecutionContext
  implicit val materializer: Materializer

  lazy val kafkaConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(kafkaHost, kafkaPort)

  def kafkaRequest(request: HttpRequest): Future[HttpResponse] =
    Source.single(request).via(kafkaConnectionFlow).runWith(Sink.head)

  def fetchTopics(): Future[Either[String, List[String]]] = {
    kafkaRequest(RequestBuilding.Get("/topics")).flatMap { response =>
      response.status match {
        case OK => {
          Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[List[String]].map(Right(_))
        }
        case BadRequest => Future.successful(Left(s"bad request"))
        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"FAIL - ${response.status}"
          Future.failed(new IOException(error))
        }
      }
    }
  }

  def fetchTopicInfo(topicName: String): Future[Either[String, String]] = {
    kafkaRequest(RequestBuilding.Get(s"/topics/$topicName")).flatMap { response =>
      response.status match {
        case OK => Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[String].map(Right(_))
        case BadRequest => Future.successful(Left(s"bad request"))
        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"FAIL - ${response.status}"
          Future.failed(new IOException(error))
        }
      }
    }
  }

  // TODO falta implementar para un plubish para json y binary
  def publishInTopicAsAvro(topicName: String, dataToPublish: String): Future[Either[String, String]] = {
    val ent = """{"records":[{"value":{"foo":"bar"}}]}"""

    val `application/vnd.kafka.avro.v1+json` = MediaType.applicationWithFixedCharset("vnd.kafka.avro.v1+json", HttpCharsets.`UTF-8`)

    val data = ByteString(dataToPublish)
    val request = HttpRequest(
                                uri = s"/topics/$topicName",
                                method = HttpMethods.POST,
                                entity =  HttpEntity(data).withContentType(`application/vnd.kafka.avro.v1+json`)
    )

    kafkaRequest(request).flatMap { response =>
      response.status match {
        case OK => {
          Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[String].map(Right(_))
        }
        case BadRequest => Future.successful(Left(s"bad request"))
        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"FAIL - ${response.status}"
          Future.failed(new IOException(error))
        }
      }
    }
  }
}
