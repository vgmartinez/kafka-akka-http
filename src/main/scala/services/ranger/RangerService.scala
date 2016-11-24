package services.ranger

import java.io.IOException

import akka.actor.ActorSystem
import akka.http.javadsl.model.headers.Authorization
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import mappings.JsonMappings
import models.PolicyEntity
import services.Base

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

/**
  * Created by victorgarcia on 21/11/16.
  */
trait RangerService extends Base with JsonMappings {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  lazy val rangerConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(rangerHost, rangerPort)

  def rangerRequest(request: HttpRequest): Future[HttpResponse] =
    Source.single(request).via(rangerConnectionFlow).runWith(Sink.head)

  def isAuthorizeForPublish(userName: String, topicName: String): Boolean = {
    var isAuthorize = false
    val futureAuthorization = fetchPublishPolicy(topicName) map { policy =>
      policy.policyItems.foreach { items =>
        items.accesses foreach { permission =>
          permission.`type` match {
            case "publish" => isAuthorize = items.users.contains(userName)
            case _ => isAuthorize = false
          }
        }
      }
    }
    Await.result(futureAuthorization, 1.seconds)
    isAuthorize
  }

  def fetchPublishPolicy(topicName: String): Future[PolicyEntity] = {
    rangerRequest(RequestBuilding.Get(s"/service/public/v2/api/service/Sandbox-kafka/policy/$topicName")
      .addHeader(Authorization.basic(rangerUser, rangerPass))).flatMap { response =>
      response.status match {
        case OK => {
          Unmarshal(response.entity.withContentType(ContentTypes.`application/json`)).to[PolicyEntity]
        }
        case BadRequest => Future.successful(null)
        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
          val error = s"FAIL - ${response.status}"
          Future.failed(new IOException(error))
        }
      }
    }
  }
}
