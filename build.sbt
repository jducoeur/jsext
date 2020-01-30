lazy val root = project.in(file(".")).
  enablePlugins(ScalaJSPlugin)

name := "jsext Library for Scala.js"

normalizedName := "querki-jsext"

version := "0.9"

organization := "org.querki"

scalaVersion := "2.13.1"

crossScalaVersions := Seq("2.10.5", "2.11.8", "2.12.8","2.13.1")

publishTo := sonatypePublishToBundle.value

usePgpKeyHex("A5A4DA66BF0A391F46AEF0EAC74603EB63699C41")

//pomIncludeRepository := { _ => false }

fork in run := true
