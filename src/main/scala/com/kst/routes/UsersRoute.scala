package com.kst.routes

import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.kst.mappings.JsonMappings
import com.kst.models.UserEntityUpdate
import spray.json._
import com.kst.services.users.UsersService._


trait UsersRoute extends JsonMappings with SecurityDirectives {
  import StatusCodes._

  val usersApi = pathPrefix("users") {
    pathEndOrSingleSlash {
      get {
        complete(findAll.map(_.toJson))
      }
    } ~
      pathPrefix("me") {
        pathEndOrSingleSlash {
          authenticate { loggedUser =>
            get {
              complete(loggedUser)
            } ~
              post {
                entity(as[UserEntityUpdate]) { userUpdate =>
                  complete(update(loggedUser.id.get, userUpdate).map(_.toJson))
                }
              }
          }
        }
    } ~
    pathPrefix(IntNumber) { id =>
      pathEndOrSingleSlash {
        authenticate { loggedUser =>
          get {
            complete(findById(id).map(_.toJson))
          } ~
          post {
            entity(as[UserEntityUpdate]) { userUpdate =>
              complete(update(id, userUpdate).map(_.toJson))
            }
          } ~
          delete {
            onSuccess(deleteUser(loggedUser.id.get)) { ignored =>
              complete(NoContent)
            }
          }
        }
      }
    }
  }
}
