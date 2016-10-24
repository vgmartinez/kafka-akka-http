package utils
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

trait DatabaseConfig extends Config{
  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(databaseUrl)
  hikariConfig.setUsername(databaseUser)
  hikariConfig.setPassword(databasePassword)
  hikariConfig.setAutoCommit(true)
  hikariConfig.setMaximumPoolSize(50)
  hikariConfig.setMinimumIdle(5)
  hikariConfig.setMaxLifetime(4)
  hikariConfig.setIdleTimeout(4)

  private val dataSource = new HikariDataSource(hikariConfig)

  val driver = slick.driver.PostgresDriver

  import driver.api._

  def db = Database.forDataSource(dataSource)

  implicit val session: Session = db.createSession()
}