package utils

import com.typesafe.config.ConfigFactory

trait Config {
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

  val rangerHost = rangerConfig.getString("default_host")
  val rangerPort = rangerConfig.getInt("default_port")
  val rangerUser = rangerConfig.getString("default_user")
  val rangerPass = rangerConfig.getString("default_pass")

}
