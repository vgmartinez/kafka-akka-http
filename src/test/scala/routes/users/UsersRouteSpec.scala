package routes.users

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpEntity, MediaTypes}
import models.UserEntity
import org.scalatest.concurrent.ScalaFutures
import services.BaseSpec
import services.users.UsersService._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.util.Random

class UsersRouteSpec extends BaseSpec with ScalaFutures{
  "Users service" should {

    "retrieve users list" in new Context {
      Get("/users") ~> usersApi ~> check {
        responseAs[Seq[UserEntity]].isEmpty should be(false)
      }
    }

    "retrieve user by id" in new Context {
      val testUser = testUsers(0)
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token

      Get(s"/users/${testUser.id.get}").withHeaders(RawHeader(header._1, header._2)) ~> usersApi ~> check {
        responseAs[UserEntity] should be(testUser)
      }
    }

    "update user by id and retrieve it" in new Context {
      val testUser = testUsers(0)
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token
      val newUsername = Random.nextString(10)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")

      Post(s"/users/${testUser.id.get}", requestEntity).withHeaders(RawHeader(header._1, header._2)) ~> usersApi ~> check {
        responseAs[UserEntity] should be(testUser.copy(username = newUsername))
        whenReady(findById(testUser.id.get)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }

    "delete user" in new Context {
      val testUser = testUsers(0)
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token
      Delete(s"/users/${testUser.id.get}").withHeaders(RawHeader(header._1, header._2)) ~> usersApi ~> check {
        response.status should be(NoContent)
        whenReady(findById(testUser.id.get)) { result =>
          result should be(None: Option[UserEntity])
        }
      }
    }

    "retrieve currently logged user" in new Context {
      val testUser = testUsers(0)
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token

      Get("/users/me").withHeaders(RawHeader(header._1, header._2)) ~> usersApi ~> check {
        responseAs[UserEntity] should be(testUsers.find(_.id.contains(testUser.id.get)).get)
      }
    }

    "update currently logged user" in new Context {
      val testUser = testUsers.head
      val newUsername = Random.nextString(10)
      val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"username": "$newUsername"}""")
      val header = "Token" -> testTokens.find(_.userId.contains(testUser.id.get)).get.token

      Post("/users/me", requestEntity).withHeaders(RawHeader(header._1, header._2)) ~> usersApi ~> check {
        responseAs[UserEntity] should be(testUsers.find(_.id.contains(testUser.id.get)).get.copy(username = newUsername))
        whenReady(findById(testUser.id.get)) { result =>
          result.get.username should be(newUsername)
        }
      }
    }
  }
}
