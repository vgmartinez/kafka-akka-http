package services

import akka.event.{LoggingAdapter, NoLogging}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import models.UserEntity
import org.scalatest._
import routes.Routes
import services.users.AuthService._
import services.users.UsersService._
import utils.InMemoryPostgresStorage._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

trait BaseSpec extends WordSpec with Matchers with ScalatestRouteTest with Routes with Base {
  protected val log: LoggingAdapter = NoLogging
  dbProcess.getProcessId

  trait Context {
    val testUsers = provisionUsersList(1)
    val testTokens = provisionTokensForUsers(testUsers)
  }

  def provisionUsersList(size: Int): Seq[UserEntity] = {
    val savedUsers = (1 to size).map { _ =>
      UserEntity(Some(Random.nextLong()), Random.nextString(10), Random.nextString(10), Random.nextInt(), Random.nextInt())
    }.map(create)

    Await.result(Future.sequence(savedUsers), 10.seconds)
  }

  def provisionTokensForUsers(usersList: Seq[UserEntity]) = {
    val savedTokens = usersList.map(createToken)
    Await.result(Future.sequence(savedTokens), 10.seconds)
  }

}
