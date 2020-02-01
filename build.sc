import mill._, scalalib._

object example extends SbtModule{
  def scalaVersion="2.13.1"

  import coursier.maven.MavenRepository

  def repositories = super.repositories ++ Seq(
    MavenRepository("https://maven.jzy3d.org/releases/")
  )

  def ivyDeps = Agg(
    ivy"org.jzy3d:jzy3d-api:1.0.2",
    ivy"org.jzy3d:jzy3d-depthpeeling:1.0.2"
  )
}
