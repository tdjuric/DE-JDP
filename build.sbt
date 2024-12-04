ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "WeatherApp"
  )
libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core" % "3.3.14", // HTTP requests
  "org.json4s" %% "json4s-native" % "4.0.3"  // parsing JSON
)




