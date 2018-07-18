

lazy val example =
  (project in file("example"))
  .settings(
    name := "Jzy3d examples",
    scalaVersion := "2.12.6",
    resolvers += "Jzy3d releases" at "http://maven.jzy3d.org/releases/",
    libraryDependencies ++= Seq(
      "org.jzy3d" % "jzy3d-api" % "1.0.0"
      )
  )
