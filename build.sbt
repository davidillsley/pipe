organization := "org.i5y"

name := "pipe"

version := "0.0.1"

scalacOptions += "-deprecation"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
        "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"
    )
    
seq(bintrayPublishSettings:_*)

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))
