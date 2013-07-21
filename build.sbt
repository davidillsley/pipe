organization := "org.i5y"

name := "pipe"

version := "0.0.1-SNAPSHOT"

scalacOptions += "-deprecation"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
        "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"
    )
    