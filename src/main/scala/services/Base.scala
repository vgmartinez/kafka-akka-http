package services

import models.definitions.{TokenEntityTable, UsersEntityTable}
import slick.dbio.NoStream
import slick.lifted.TableQuery
import slick.profile.{FixedSqlStreamingAction, SqlAction}
import scala.concurrent.Future
import utils.DatabaseConfig

trait Base extends DatabaseConfig {
  val usersTable = TableQuery[UsersEntityTable]
  val tokenTable = TableQuery[TokenEntityTable]

  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }
  protected implicit def executeReadStreamFromDb[A](action: FixedSqlStreamingAction[Seq[A], A, _ <: slick.dbio.Effect]): Future[Seq[A]] = {
    db.run(action)
  }
}
