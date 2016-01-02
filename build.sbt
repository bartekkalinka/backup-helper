name := "backup-helper"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= List(
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.0.1",
  "org.scalatest" %% "scalatest" % "2.2.5" % "test"
)