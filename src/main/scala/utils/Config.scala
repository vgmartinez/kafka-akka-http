package utils

trait Config {
  val httpInterface = System.getenv("COGNOS_HTTP_INTERFACE")
  val httpPort = System.getenv("COGNOS_HTTP_PORT").toInt

  val databaseUrl = System.getenv("COGNOS_DATABASE_URL")
  val databaseUser = System.getenv("COGNOS_DATABASE_USER")
  val databasePassword = System.getenv("COGNOS_DATABASE_PASSWORD")

  val kafkaHost = System.getenv("COGNOS_KAFKA_HOST")
  val kafkaPort = System.getenv("COGNOS_KAFKA_PORT").toInt

  val rangerHost = System.getenv("COGNOS_RANGER_HOST")
  val rangerPort = System.getenv("COGNOS_RANGER_PORT").toInt
  val rangerUser = System.getenv("COGNOS_RANGER_USER")
  val rangerPass = System.getenv("COGNOS_RANGER_PASSWORD")

}
