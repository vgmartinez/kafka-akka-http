package com.kst.utils

import de.flapdoodle.embed.process.runtime.Network._
import ru.yandex.qatools.embed.postgresql.PostgresStarter
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.{Credentials, Net, Storage, Timeout}
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig
import ru.yandex.qatools.embed.postgresql.distribution.Version

object InMemoryPostgresStorage extends MigrationConfig{
  val dbHost = getLocalHost.getHostAddress
  val dbPort = 5432
  val dbName = "postgres"
  val dbUser = "postgres"
  val dbPassword = "password"
  val jdbcUrl = s"jdbc:postgresql://$dbHost:$dbPort/$dbName"

  lazy val dbProcess = {
    val psqlConfig = new PostgresConfig(
      Version.V9_5_0, "test"
    )
    val psqlInstance = PostgresStarter.getDefaultInstance

    val process = psqlInstance.prepare(psqlConfig).start()

    reloadSchema()
    process
  }
}