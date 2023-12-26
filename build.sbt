ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "Flink Project"

version := "0.1-SNAPSHOT"

organization := "org.example"

ThisBuild / scalaVersion := "2.12.7"

val flinkVersion = "1.16.0"


lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= Seq(
      "org.apache.flink" % "flink-clients" % flinkVersion ,
      "org.apache.flink" %% "flink-scala" % flinkVersion ,
      "org.apache.flink" %% "flink-streaming-scala" % flinkVersion ,
      "org.apache.flink" % "flink-connector-kafka" % flinkVersion,


      "org.slf4j" % "slf4j-api" % "2.0.9",
      "org.slf4j" % "slf4j-simple" % "2.0.9",

    )
  )

assembly / mainClass := Some("org.example.Job")

// make run command include the provided dependencies
Compile / run := Defaults.runTask(Compile / fullClasspath,
  Compile / run / mainClass,
  Compile / run / runner
).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false)
