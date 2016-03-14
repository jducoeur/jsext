import SonatypeKeys._
import sbt.Keys._

lazy val commonSettings = sonatypeSettings ++ Seq(
  version := "0.6",
  organization := "org.querki",
  scalaVersion := "2.11.7",
  crossScalaVersions := Seq("2.10.4", "2.11.7"),
  scalacOptions += "-feature",
  homepage := Some(url("http://www.querki.net/")),
  licenses += ("MIT License", url("http://www.opensource.org/licenses/mit-license.php")),
  scmInfo := Some(ScmInfo(
    url("https://github.com/jducoeur/jsext"),
    "scm:git:git@github.com/jducoeur/jsext.git",
    Some("scm:git:git@github.com/jducoeur/jsext.git"))),
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra := (
    <developers>
      <developer>
        <id>jducoeur</id>
        <name>Mark Waks</name>
        <url>https://github.com/jducoeur/</url>
      </developer>
    </developers>
    <contributors>
      <contributor>
        <name>Jasper Moeys</name>
        <url>https://github.com/Jasper-M/</url>
      </contributor>
      <contributor>
        <name>Stefan Larsson</name>
        <url>https://github.com/lastsys/</url>
      </contributor>
      <contributor>
        <id>mseddon</id>
        <name>Matt Seddon</name>
        <url>https://github.com/mseddon/</url>
      </contributor>
    </contributors>
  ),
  pomIncludeRepository := { _ => false }
)

lazy val root = project.in(file(".")).settings(commonSettings: _*).settings(
  name := "jsext Library for Scala.js",
  normalizedName := "querki-jsext"
).enablePlugins(ScalaJSPlugin)

lazy val dom = project.in(file("dom")).settings(commonSettings: _*).settings(
  name := "jsext Dom Library for Scala.js",
  normalizedName := "querki-jsext-dom",
  libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"
).enablePlugins(ScalaJSPlugin)