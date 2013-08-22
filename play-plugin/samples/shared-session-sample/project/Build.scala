import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-rails-session-bridge-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "play-rails-session-bridge" % "play-rails-session-bridge_2.10" % "1.0-SNAPSHOT",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
  )

}
