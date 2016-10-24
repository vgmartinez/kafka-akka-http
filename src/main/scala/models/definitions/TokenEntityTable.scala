package models.definitions

import models.TokenEntity
import services.Base
import slick.driver.PostgresDriver.api._

class TokenEntityTable (tag: Tag) extends Table[TokenEntity](tag, "tokens") with Base {
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Option[Long]]("user_id")
  def token = column[String]("token")

  def userFk = foreignKey("USER_FK", userId, usersTable)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

  def * = (id, userId, token) <> ((TokenEntity.apply _).tupled, TokenEntity.unapply)
}
