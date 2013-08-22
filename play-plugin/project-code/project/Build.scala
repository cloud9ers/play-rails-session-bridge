import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "play-rails-session-bridge"
  val appVersion      = "1.0-SNAPSHOT"
  val appScalaVersion = "2.10.0"
  val appScalaBinaryVersion = "2.10"
  val appScalaCrossVersions = Seq("2.10.0")

  val appDependencies = Seq(
    "spy" % "spymemcached" % "2.8.9",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
      resolvers += "Spy Repository" at "http://files.couchbase.com/maven2" 
  )

}
