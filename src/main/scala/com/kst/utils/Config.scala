package com.kst.utils

import com.typesafe.config.ConfigFactory

trait Config {
  var httpInterface = ""
  var httpPort = 0
  var kafkaHost = ""
  var kafkaPort = 0
  var kafkaSecurityProtocol = ""
  var rangerHost = ""
  var rangerPort = 0
  var rangerUser = ""
  var rangerPass = ""
  var rangerRepository = ""

  private val config = ConfigFactory.load("application.conf")
  if (!config.isEmpty) {
    // leyendo las variables desde fichero
    val httpConfig = config.getConfig("http")
    val kafkaConfig = config.getConfig("kafka")
    val rangerConfig = config.getConfig("ranger")

    httpInterface = httpConfig.getString("interface")
    httpPort = httpConfig.getInt("port")
    kafkaHost = kafkaConfig.getString("default_host")
    kafkaPort = kafkaConfig.getInt("default_port")
    kafkaSecurityProtocol = kafkaConfig.getString("security_protocol")
    rangerHost = rangerConfig.getString("default_host")
    rangerPort = rangerConfig.getInt("default_port")
    rangerUser = rangerConfig.getString("default_user")
    rangerPass = rangerConfig.getString("default_pass")
    rangerRepository = rangerConfig.getString("default_repository")
  } else {
    // leyendo las variables desde consul
    System.setProperty("java.security.auth.login.config", "/usr/src/app/kafka_jaas.conf")
    System.setProperty("javax.security.auth.useSubjectCredsOnly", "false")
    System.setProperty("sun.security.krb5.debug", "true")
    System.setProperty("javax.net.debug", "all")

    httpInterface = System.getenv("COGNOS_HTTP_INTERFACE")
    httpPort = System.getenv("COGNOS_HTTP_PORT").toInt
    kafkaHost = System.getenv("COGNOS_KAFKA_HOST")
    kafkaPort = System.getenv("COGNOS_KAFKA_PORT").toInt
    kafkaSecurityProtocol = System.getenv("COGNOS_KAFKA_SECURITY_PROTOCOL")
    rangerHost = System.getenv("COGNOS_RANGER_HOST")
    rangerPort = System.getenv("COGNOS_RANGER_PORT").toInt
    rangerUser = System.getenv("COGNOS_RANGER_USER")
    rangerPass = System.getenv("COGNOS_RANGER_PASSWORD")
    rangerRepository = System.getenv("COGNOS_RANGER_REPOSITORY")
  }
}
