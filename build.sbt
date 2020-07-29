name := "closer"
version := "0.1"

scalaVersion := "2.13.3"
val akkaVersion = "2.6.8"


libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.12"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % akkaVersion
libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-json" % "2.1.2"
libraryDependencies += "de.heikoseeberger" %% "akka-http-play-json" % "1.31.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.1.12"
