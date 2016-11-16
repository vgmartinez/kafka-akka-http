import sbt.Keys.{artifactPath, libraryDependencies, mainClass, managedClasspath, name, organization, packageBin, resolvers, version}
import NativePackagerHelper._

name := "kafkaRestApi"

version := "1.0.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaStreamVersion = "2.0.5"
  val akkaVersion = "2.4.11"
  val scalaTestVersion       = "3.0.0"
  val scalaMockVersion       = "3.3.0"
  val slickVersion = "3.1.1"

  Seq(
    "com.typesafe.akka" %% "akka-actor"                           % akkaVersion,
    "com.typesafe.akka" %% "akka-stream-experimental"             % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-experimental"               % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-core-experimental"          % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"    % akkaStreamVersion,
    "org.apache.kafka"   % "kafka-clients"                        % "0.10.0.0",
    "com.typesafe.slick"%% "slick"                                % slickVersion,
    "com.typesafe.play" %% "play-slick-evolutions"                % "2.0.2",
    "org.apache.hadoop"  % "hadoop-client"                        % "2.7.3",
    "org.postgresql"     %  "postgresql"                           % "9.4-1201-jdbc41",
    "org.flywaydb"       %  "flyway-core"                          % "3.2.1",
    "com.typesafe.akka"  %% "akka-testkit"                         % akkaVersion % "test",
    "ru.yandex.qatools.embed" % "postgresql-embedded"              % "1.15" % "test",
    "org.scalatest"      %% "scalatest"                            % scalaTestVersion % "test",
    "org.scalamock"      %% "scalamock-scalatest-support"          % scalaMockVersion % "test",
    "com.typesafe.akka"  %% "akka-http-testkit-experimental"       % akkaStreamVersion
  )
}