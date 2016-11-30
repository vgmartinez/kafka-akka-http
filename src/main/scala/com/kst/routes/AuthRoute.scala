package com.kst.routes

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import com.kst.mappings.JsonMappings
import com.kst.models.{LoginPassword, UserEntity}
import com.kst.services.users.AuthService._
import spray.json._

trait AuthRoute extends JsonMappings with SecurityDirectives {
  val authRoute = pathPrefix("auth") {
    pathPrefix("signIn") {
      pathEndOrSingleSlash {
        post {
          entity(as[LoginPassword]) { login =>
            complete(signIn(login.login, login.password).map(_.toJson))
          }
        }
      }
    }~
    pathPrefix("signUp") {
      pathEndOrSingleSlash {
        post {
          entity(as[UserEntity]) { user =>
            complete(Created -> signUp(user).map(_.toJson))
          }
        }
      }
    }
  }
}