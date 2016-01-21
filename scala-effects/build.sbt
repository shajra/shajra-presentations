tutSettings


scalaVersion := "2.11.7"

val scalazV = "7.1.4"
val scalazStreamV = "0.8"


scalacOptions ++= List("-feature", "-language:postfixOps")

watchSources <++= tutSourceDirectory map { path => (path ** "*.html").get }

libraryDependencies ++=
  List(
    "org.scalaz" %% "scalaz-core" % scalazV,
    "org.scalaz" %% "scalaz-concurrent" % scalazV,
    "org.scalaz.stream" %% "scalaz-stream" % scalazStreamV)
