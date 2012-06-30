import sbt._
import sbt.Keys._

object EventsBuild extends Build {

  lazy val events = Project(
    id = "events",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "events",
      organization := "com.livestream",
      version := "0.2-SNAPSHOT",
      scalaVersion := "2.9.1"
      // add other settings here
    )
  )
}
