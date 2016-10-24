package routes.hdfs

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.headers.RawHeader
import org.scalatest.concurrent.ScalaFutures
import services.BaseSpec

class KafkaRouteSpec extends BaseSpec with ScalaFutures{
  "Hdfs service" should {

    "retrieve directory list" in new Context {
      val testUser = testUsers(0)
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token

      Get("/hdfs/list?path=tmp").withHeaders(RawHeader(header._1, header._2)) ~> hdfsApi ~> check {
        println(response)
        response.status should be(StatusCodes.OK)
      }
    }
  }
}
