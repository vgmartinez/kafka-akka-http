package models.definitions

import models.{UserId, UserEntity}
import slick.driver.PostgresDriver.api._

class UsersEntityTable(tag: Tag) extends Table[UserEntity](tag, "users"){
  def id = column[UserId]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def password = column[String]("password")
  def age = column[Int]("age")
  def gender = column[Int]("gender")
  def * = (id.?, username, password, age, gender) <> ((UserEntity.apply _).tupled, UserEntity.unapply)
}

