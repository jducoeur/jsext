lazy val root = project.in(file(".")).
  enablePlugins(ScalaJSPlugin)

name := "jsext Library for Scala.js"

normalizedName := "querki-jsext"

version := "0.12"

organization := "org.querki"

ThisBuild / scalacOptions ++= Seq("-feature", "-deprecation")

scalaVersion := "3.1.0"

crossScalaVersions := Seq("2.12.8","2.13.1", "3.1.0")

publishTo := sonatypePublishToBundle.value

usePgpKeyHex("A5A4DA66BF0A391F46AEF0EAC74603EB63699C41")

//pomIncludeRepository := { _ => false }

run / fork := true
