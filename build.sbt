

lazy val example =
  (project in file("example"))
  .settings(
    name := "Jzy3d examples",
    scalaVersion := "2.13.1",
    resolvers += "Jzy3d releases" at "https://maven.jzy3d.org/releases/",
    libraryDependencies ++= Seq(
      "org.jzy3d" % "jzy3d-api" % "1.0.0"
      )
  )
