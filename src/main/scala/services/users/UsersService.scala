package services.users

import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import models.{UserEntity, UserEntityUpdate, UserId}
import services.Base

object UsersService extends Base {
  def findAll: Future[Seq[UserEntity]] = usersTable.result
  def findById(userId: UserId): Future[Option[UserEntity]] = usersTable.filter(_.id === userId).result.headOption
  def create(user: UserEntity): Future[UserEntity] = usersTable returning usersTable += user
  def update(id: Long, userUpdate: UserEntityUpdate): Future[Option[UserEntity]]  = findById(id).flatMap {
    case Some(user) =>
      val updatedUser = userUpdate.merge(user)
      db.run(usersTable.filter(_.id === id).update(updatedUser)).map(_ => Some(updatedUser))
    case None => Future.successful(None)
  }
  def deleteUser(userId: UserId): Future[Int] = usersTable.filter(_.id === userId).delete

}
