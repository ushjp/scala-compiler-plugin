import sbt._
import sbt.Keys._

/**
 * 全プロジェクト共通の設定
 */
object Common {
  val commonSettings = Seq(
    // organization := "ushjp",
    version := "0.1.0",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-encoding", "utf-8",
      "-target:jvm-1.8",
      "-deprecation",
      "-unchecked",
      "-Xlint",
      "-feature"
    ),
    javacOptions ++= Seq(
      "-encoding", "utf-8",
      "-source", "1.8",
      "-target", "1.8"
    ),
    resolvers ++= Dependencies.resolutionRepos
  )

  /**
   * テスト設定
   */
  val testSettings = Seq(
    fork in Test := true,
    javaOptions in Test ++= sys.process.javaVmArguments.filter(a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith))
  )

  /**
   * sbtプロンプトに現在のプロジェクト名を表示
   */
  val promptSettings = Seq(
    shellPrompt := { s => s"${Project.extract(s).currentProject.id}> "}
  )

  /**
   * sbtプロジェクトの作成
   * @param name プロジェクトの名前
   * @param path プロジェクトルートのパス (None指定時はプロジェクトの名前をパスとしても使う)
   * @return Projectインスタンス
   */
  def createProject(name: String, path: Option[String] = None) = Project(name, file(path.getOrElse(name)))
    .settings(commonSettings ++ testSettings ++ promptSettings)

}
