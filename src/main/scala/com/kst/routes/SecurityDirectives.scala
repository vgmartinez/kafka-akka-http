package com.kst.routes

import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.{BasicDirectives, FutureDirectives, HeaderDirectives, RouteDirectives}

trait SecurityDirectives {
  import BasicDirectives._
  import HeaderDirectives._
  import RouteDirectives._
  import FutureDirectives._
/*
  def authenticate: Directive1[UserEntity] = {
    headerValueByName("Token").flatMap { token =>
      onSuccess(AuthService.authenticate(token)).flatMap {
        case Some(user) => provide(user)
        case None       => reject
      }
    }
  }*/
}
