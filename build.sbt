ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.11.12"

lazy val root = (project in file("."))
  .settings(
    name := "LoadJson",
    idePackagePrefix := Some("com.galamome")
  )

val sparkVersion = "2.3.2"

libraryDependencies ++= Seq(
  // Exact version (only one %) to force to be in old version of Spark
  "org.apache.spark" % "spark-core" % sparkVersion,
  "org.apache.spark" % "spark-sql" % sparkVersion
)
