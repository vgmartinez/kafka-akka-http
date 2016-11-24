package utils

import de.flapdoodle.embed.process.runtime.Network._
import ru.yandex.qatools.embed.postgresql.PostgresStarter
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig.{Credentials, Net, Storage, Timeout}
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig
import ru.yandex.qatools.embed.postgresql.distribution.Version

object InMemoryPostgresStorage extends MigrationConfig{
  val dbHost = getLocalHost.getHostAddress

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