import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "shared-session-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "shared-session" % "shared-session_2.10" % "1.0-SNAPSHOT",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
        resolvers += "Local Play Repository" at "file://home/ashihaby/programs/play-2.1.0/play-2.0/repository/local"    
  )

}
