package services.users

import models.{TokenEntity, UserEntity}
import slick.driver.PostgresDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import services.Base
import UsersService._

object AuthService extends Base {

  def signIn(login: String, password: String): Future[Option[TokenEntity]] = {
    db.run(usersTable.filter(u => u.username === login).result).flatMap { users =>
      users.find(user => user.password == password) match {
        case Some(user) => db.run(tokenTable.filter(_.userId === user.id).result.headOption).flatMap {
          case Some(token) => Future.successful(Some(token))
          case None        => createToken(user).map(token => Some(token))
        }
        case None => Future.successful(None)
      }
    }
  }

  def signUp(newUser: UserEntity): Future[TokenEntity] = {
    create(newUser).flatMap(user => createToken(user))
  }

  def authenticate(token: String): Future[Option[UserEntity]] =
    db.run((for {
      token <- tokenTable.filter(_.token === token)
      user <- usersTable.filter(_.id === token.userId)
    } yield user).result.headOption)

  def createToken(user: UserEntity): Future[TokenEntity] = db.run(tokenTable returning tokenTable += TokenEntity(userId = user.id))

}
