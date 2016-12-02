import sbt.Keys.{artifactPath, libraryDependencies, mainClass, managedClasspath, name, organization, packageBin, resolvers, version}
import NativePackagerHelper._

name := "kafkaRestApi"

version := "1.0.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaStreamVersion = "2.0.5"
  val akkaVersion = "2.4.11"
  val kafkaVersion = "0.10.1.0"
  val scalaTestVersion = "3.0.0"
  val scalaMockVersion = "3.3.0"
  val slickVersion = "3.1.1"
  val playSlickVersion = "2.0.2"
  val hadoopVersion = "2.7.3"
  val embeddedKafkaVersion = "0.10.0"
  val postgresVersion = "1.15"
  val flywayVersion = "3.2.1"
  val postgresJDBC = "9.4-1201-jdbc41"
  val avro4sVersion = "1.6.2"

  Seq(
    "com.typesafe.akka"         %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka"         %% "akka-stream-experimental"             % akkaStreamVersion,
    "com.typesafe.akka"         %% "akka-http-experimental"               % akkaStreamVersion,
    "com.typesafe.akka"         %% "akka-http-core-experimental"          % akkaStreamVersion,
    "com.typesafe.akka"         %% "akka-http-spray-json-experimental"    % akkaStreamVersion,
    "org.apache.kafka"          %% "kafka"                                % kafkaVersion,
    "com.typesafe.slick"        %% "slick"                                % slickVersion,
    "com.typesafe.play"         %% "play-slick-evolutions"                % playSlickVersion,
    "org.apache.hadoop"          % "hadoop-client"                        % hadoopVersion,
    "com.sksamuel.avro4s"        % "avro4s-core_2.11"                     % avro4sVersion,
    "org.postgresql"             %  "postgresql"                          % postgresJDBC,
    "org.flywaydb"               %  "flyway-core"                         % flywayVersion,
    "com.typesafe.akka"         %% "akka-testkit"                         % akkaVersion       % "test",
    "ru.yandex.qatools.embed"    % "postgresql-embedded"                  % postgresVersion   % "test",
    "org.scalatest"             %% "scalatest"                            % scalaTestVersion  % "test",
    "org.scalamock"             %% "scalamock-scalatest-support"          % scalaMockVersion  % "test",
    "com.typesafe.akka"         %% "akka-http-testkit-experimental"       % akkaStreamVersion % "test",
    "net.manub"                 %% "scalatest-embedded-kafka"             % embeddedKafkaVersion
  )
}

parallelExecution in Test := false

assemblyExcludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  val excludes = Set(
    "commons-logging-1.1.3.jar",
    "commons-beanutils-core-1.8.0",
    "akka-stream_2.11-2.4.2.jar",
    "commons-collections-3.2.2.jar",
    "stax-api-1.0-2.jar",
    "commons-beanutils-1.7.0.jar"
  )
  cp filter {
    jar => excludes(jar.data.getName)
  }
}