name := "closer"
version := "0.1"

scalaVersion := "2.13.3"
val akkaVersion = "2.6.8"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.0",
  "org.scalatest" %% "scalatest" % "3.2.0" % "test",
  "com.typesafe.akka" %% "akka-http" % "10.1.12",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.play" %% "play-ws-standalone-json" % "2.1.2",
  "de.heikoseeberger" %% "akka-http-play-json" % "1.31.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.12"
)