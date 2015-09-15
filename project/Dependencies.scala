import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "spray repo" at "http://repo.spray.io",
    "SauceLabs" at "http://repository-saucelabs.forge.cloudbees.com/release/"
  )

  def compile(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")

  def provided(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")

  def test(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")

  def runtime(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")

  def container(deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  // Libraries
  val compiler = "org.scala-lang" % "scala-compiler" % "2.11.7"

  // Test
  val scalatest = "org.scalatest" %% "scalatest" % "2.2.5"
}
