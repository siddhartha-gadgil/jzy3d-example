package surface

import org.jzy3d.chart.Chart;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives._
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.chart._
import org.jzy3d.plot3d.rendering.canvas.Quality

import scala.jdk.CollectionConverters._

import scala.math.{sin, cos, Pi, sqrt}
import org.jzy3d.colors.colormaps.ColorMapHotCold
import org.jzy3d.colors.Color
import scala.util._
import org.jzy3d.chart.factories.AWTChartComponentFactory
import org.jzy3d.plot3d.rendering.view.AWTRenderer3d
import javax.imageio.ImageIO
import java.awt.image.BufferedImage

case class PointVector(x: Double, y: Double, z: Double) {
  lazy val point: Point = new Point(new Coord3d(x, y, z))

  def scale(sc: Double) = PointVector(x * sc, y * sc, z * sc)

  def -(that: PointVector) = PointVector(x - that.x, y - that.y, z - that.z)

  def +(that: PointVector) = PointVector(x + that.x, y + that.y, z + that.z)

  def *:(sc: Double) = scale(sc)
}

case class Square(
    a: PointVector,
    b: PointVector,
    c: PointVector,
    d: PointVector,
    colour: Color = Color.MAGENTA
) {
  def scale(sc: Double) =
    Square(a.scale(sc), b.scale(sc), c.scale(sc), d.scale(sc))

  val vertices : Vector[Point] = Vector(a, b, d, c).map(_.point)

  lazy val polygon : Polygon = {
      val poly = new Polygon()
      vertices.foreach(p => poly.add(p))
      poly.setColor(colour)
      poly.setFaceDisplayed(false)
      poly.setWireframeColor(org.jzy3d.colors.Color.MAGENTA)
      poly
  }

  val xVec = b - a

  val yVec = c - a

  def split(n: Int): Vector[Square] = {
    def gridPoint(i: Int, j: Int) =
      a + ((i.toDouble / n) *: xVec) + ((j.toDouble / n) *: yVec)
    def subSquare(i: Int, j: Int): Square =
      Square(
        gridPoint(i, j),
        gridPoint(i + 1, j),
        gridPoint(i, j + 1),
        gridPoint(i + 1, j + 1)
      )
    (for {
      i <- 0 until n
      j <- 0 until n
    } yield subSquare(i, j)).toVector
  }
}

object SquareGrid{
    val base = Vector(
        Square(PointVector(0, 0, 0), PointVector(1, 0, 0), PointVector(0, 1, 0), PointVector(1, 1, 0)),
        Square(PointVector(0, 0, 1), PointVector(1, 0, 1), PointVector(0, 1, 1), PointVector(1, 1, 1)),
        Square(PointVector(0, 0, 0), PointVector(0, 1, 0), PointVector(0, 0, 1), PointVector(0, 1, 1)),
        Square(PointVector(1, 0, 0), PointVector(1, 1, 0), PointVector(1, 0, 1), PointVector(1, 1, 1)),
        Square(PointVector(0, 0, 0), PointVector(0, 0, 1), PointVector(1, 0, 0), PointVector(1, 0, 1)),
        Square(PointVector(0, 1, 0), PointVector(0, 1, 1), PointVector(1, 1, 0), PointVector(1, 1, 1))
    )


    val squares = base.flatMap(_.split(20))

    lazy val shape: Shape = {
        val surf = new Shape(squares.map(_.polygon).asJava)
        surf.setWireframeDisplayed(true);
        surf
    }
}
