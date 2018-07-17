import mill._, scalalib._

object example extends ScalaModule{
  def scalaVersion="2.12.6"

  import coursier.maven.MavenRepository

  def repositories = super.repositories ++ Seq(
    MavenRepository("http://maven.jzy3d.org/releases/")
  )

  def ivyDeps = Agg(
    ivy"org.jzy3d:jzy3d-api:1.0.0"
  )
}
