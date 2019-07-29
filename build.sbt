lazy val root = project.in(file(".")).
  enablePlugins(ScalaJSPlugin)

name := "jsext Library for Scala.js"

normalizedName := "querki-jsext"

version := "0.9"

organization := "org.querki"

scalaVersion := "2.12.8"

crossScalaVersions := Seq("2.10.5", "2.11.8", "2.12.8","2.13.0")

homepage := Some(url("http://www.querki.net/"))

licenses += ("MIT License", url("http://www.opensource.org/licenses/mit-license.php"))

scmInfo := Some(ScmInfo(
    url("https://github.com/jducoeur/jsext"),
    "scm:git:git@github.com/jducoeur/jsext.git",
    Some("scm:git:git@github.com/jducoeur/jsext.git")))

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

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
  </contributors>
)

pomIncludeRepository := { _ => false }


fork in run := true
