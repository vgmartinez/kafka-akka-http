package routes.users

import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import models.{TokenEntity, UserEntity}
import services.BaseSpec
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

/**
  * Created by victorgarcia on 13/10/16.
  */
class AuthRouteSpec extends BaseSpec {

  "Auth service" should {

    "register users and retrieve token" in new Context {
      val testUser = testUsers(0)
      signUpUser(testUser) {
        response.status should be(StatusCodes.Created)
      }
    }

    "authorize users by login and password and retrieve token" in new Context {
      val testUser = testUsers(0)
      signInUser(testUser) {
        responseAs[TokenEntity] should be
      }
    }
  }

  private def signUpUser(user: UserEntity)(action: => Unit) = {
    val requestEntity = HttpEntity(MediaTypes.`application/json`, user.toJson.toString())
    Post("/auth/signUp", requestEntity) ~> authRoute ~> check(action)
  }

  private def signInUser(user: UserEntity)(action: => Unit) = {
    val requestEntity = HttpEntity(
      MediaTypes.`application/json`,
      s"""{"login": "${user.username}", "password": "${user.password}"}"""
    )
    Post("/auth/signIn", requestEntity) ~> authRoute ~> check(action)
  }
}
