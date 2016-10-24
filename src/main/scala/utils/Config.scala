package utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val databaseConfig = config.getConfig("database")
  private val kafkaConfig = config.getConfig("kafka")

  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val databaseUrl = databaseConfig.getString("url")
  val databaseUser = databaseConfig.getString("user")
  val databasePassword = databaseConfig.getString("password")

  val kafkaHost = kafkaConfig.getString("default_host")
  val kafkaPort = kafkaConfig.getInt("default_port")
  val kafkaUserName = kafkaConfig.getString("default_username")
  val kafkaPassword = kafkaConfig.getString("default_password")
  val kafkaProtocol = kafkaConfig.getString("default_protocol")
}
