
name := "winner"
organization := "com.quirl"
maintainer := "quirl"
version := "0.0.0"

scalaVersion := "2.13.0"
lazy val playVersion = "2.7.3"
lazy val akkaVersion = "2.5.23"

libraryDependencies += guice
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j"        % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"       % akkaVersion,
  "com.typesafe.akka" %% "akka-actor"        % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka" % "1.1.0"
)
libraryDependencies += "com.typesafe.play" %% "play" % playVersion
libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "2.0.0-RC2"
libraryDependencies += "org.apache.avro" % "avro" % "1.9.2"

enablePlugins(PlayScala)

// If there is a weird issue during some builds, disable Coursier
//ThisBuild / useCoursier := false

// Code coverage.
coverageExcludedPackages := ""
scapegoatVersion in ThisBuild := "1.4.1"
scapegoatIgnoredFiles := Seq(".*Routes.scala")
scapegoatDisabledInspections := Seq("PartialFunctionInsteadOfMatch")
// scapegoat failure on error does not seem to work: https://github.com/sksamuel/sbt-scapegoat/issues/72, but its rules are interesting
scalastyleFailOnError := true
scalastyleFailOnWarning := true




//Test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "3.1.0" % Test
libraryDependencies += "net.javacrumbs.json-unit" % "json-unit" % "2.9.0" % Test
Test / parallelExecution := false

