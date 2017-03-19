val scalaV = "2.12.1"
val catsV = "0.9.0"
val scalazV = "7.2.9"
val kindProjectorV = "0.9.3"
val effV = "3.1.0"


val globalSettings =
  addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorV) ++
    List(
      scalaVersion := scalaV,
      scalacOptions ++= List(
        "-encoding", "UTF-8",
        "-feature",
        "-language:existentials",
        "-language:higherKinds",
        "-language:implicitConversions",
        "-language:postfixOps",
        "-target:jvm-1.8",
        "-Xfuture",
        "-Ypartial-unification",
        "-Yno-adapted-args"))


lazy val root = project
  .in(file("."))
  .settings(globalSettings)
  .aggregate(explore, presentation)


lazy val explore = project
  .in(file("explore"))
  .dependsOn(meta)
  .settings(globalSettings)


lazy val meta = project
  .in(file("meta"))
  .settings(globalSettings)
  .settings(
    libraryDependencies ++=
      List(
        "org.scala-lang" % "scala-reflect" % scalaVersion.value
        , "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"))


lazy val presentation = project
  .in(file("presentation"))
  .dependsOn(explore)
  .settings(globalSettings)
  .settings(tutSettings)
  .settings(
    watchSources :=
      (tutSourceDirectory.value ** ("*.md" || "*.tex"))
        .filter { _.toPath.getFileName.toString.matches("^[a-zA-Z0-9].+") }
        .get,
    libraryDependencies ++=
      List("org.typelevel" %% "cats" % catsV, "org.atnos" %% "eff" % effV))
