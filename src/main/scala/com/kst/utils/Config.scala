package com.kst.utils

import com.typesafe.config.ConfigFactory

trait Config {
  /*
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val databaseConfig = config.getConfig("database")
  private val kafkaConfig = config.getConfig("kafka")
  private val rangerConfig = config.getConfig("ranger")

  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val databaseUrl = databaseConfig.getString("url")
  val databaseUser = databaseConfig.getString("user")
  val databasePassword = databaseConfig.getString("password")

  val kafkaHost = kafkaConfig.getString("default_host")
  val kafkaPort = kafkaConfig.getInt("default_port")
  val kafkaSecurityProtocol = kafkaConfig.getString("security_protocol")

  val rangerHost = rangerConfig.getString("default_host")
  val rangerPort = rangerConfig.getInt("default_port")
  val rangerUser = rangerConfig.getString("default_user")
  val rangerPass = rangerConfig.getString("default_pass")
*/
  val httpInterface = System.getenv("COGNOS_HTTP_INTERFACE")
  val httpPort = System.getenv("COGNOS_HTTP_PORT").asInstanceOf[Int]

  val databaseUrl = System.getenv("COGNOS_DATABASE_URL")
  val databaseUser = System.getenv("COGNOS_DATABASE_USER")
  val databasePassword = System.getenv("COGNOS_DATABASE_PASSWORD")
  val databaseDriver = System.getenv("COGNOS_DATABASE_DRIVER")

  val kafkaHost = System.getenv("COGNOS_KAFKA_HOST")
  val kafkaPort = System.getenv("COGNOS_KAFKA_PORT").asInstanceOf[Int]
  val kafkaSecurityProtocol = System.getenv("COGNOS_KAFKA_SECURITY_PROTOCOL")

  val rangerHost = System.getenv("COGNOS_RANGER_HOST")
  val rangerPort = System.getenv("COGNOS_RANGER_PORT").asInstanceOf[Int]
  val rangerUser = System.getenv("COGNOS_RANGER_USER")
  val rangerPass = System.getenv("COGNOS_RANGER_PASSWORD")

}
