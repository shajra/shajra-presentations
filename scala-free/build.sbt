tutSettings

scalaVersion := "2.11.7"

val scalazV = "7.1.6"

scalacOptions ++=
  List(
    "-feature",
    "-language:postfixOps",
    "-language:higherKinds",
    "-deprecation:false")

watchSources <++= tutSourceDirectory map { path => (path ** "*.html").get }

libraryDependencies ++=
  List(
    "org.scalaz" %% "scalaz-core" % scalazV,
    "org.scalaz" %% "scalaz-concurrent" % scalazV)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")
