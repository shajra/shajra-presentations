scalaVersion := "2.11.8"
val scalazV = "7.1.7"
val kindProjectorV = "0.7.1"
val knobsV = "3.3.3"

scalacOptions ++= List(
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-target:jvm-1.8",
  "-Xfuture",
  "-Yno-adapted-args")

tutSettings
watchSources :=
  (tutSourceDirectory.value ** ("*.md" || "*.tex"))
    .filter { _.toPath.getFileName.toString.matches("^[a-zA-Z0-9].+") }
    .get

libraryDependencies ++=
  List(
    "org.scalaz" %% "scalaz-core" % scalazV,
    "oncue.knobs" %% "core" % knobsV)

addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorV)
