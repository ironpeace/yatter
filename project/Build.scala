import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "origin_yatter"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "com.github.aselab" %% "scala-activerecord" % "0.2.2",
    "org.slf4j" % "slf4j-nop" % "1.7.2",
    jdbc,
    "com.h2database" % "h2" % "1.3.170"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases")
    )
  )

}
