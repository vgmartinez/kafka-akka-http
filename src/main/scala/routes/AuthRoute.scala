package routes

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import mappings.JsonMappings
import models.{LoginPassword, UserEntity}
import services.users.AuthService._
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
