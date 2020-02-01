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

object Torus {
  val torusScale = 3.0

  val (xv, yv, zv) = (1.0, 1.0, 10.0)

  def point(x: Double, y: Double, z: Double): Point =
    new Point(new Coord3d(x, y, z))

  def torusPoint(u: Double, v: Double, sc: Double = 1): Point = {
    val z = cos(v * 2 * Pi)
    val x = sin(2 * Pi * u) * (torusScale + sin(2 * Pi * v))
    val y = cos(2 * Pi * u) * (torusScale + sin(2 * Pi * v))
    point(x, y, z)
  }

  def torusCoords(u: Double, v: Double, r: Double = 1): Coord3d = {
    val z = r * cos(v * 2 * Pi)
    val x = sin(2 * Pi * u) * (torusScale + (r * sin(2 * Pi * v)))
    val y = cos(2 * Pi * u) * (torusScale + (r * sin(2 * Pi * v)))
    new Coord3d(x, y, z)
  }

  def torusNormal(u: Double, v: Double) = {
    val z = cos(v * 2 * Pi)
    val x = sin(2 * Pi * u) * (0 + sin(2 * Pi * v))
    val y = cos(2 * Pi * u) * (0 + sin(2 * Pi * v))
    (x, y, z)
  }

  def isVisible(u: Double, v: Double): Boolean = {
    val p = torusCoords(u, v)
    val (xn, yn, zn) = torusNormal(u, v)
    ((xv - p.x) * xn) + ((yv - p.y) * yn) + ((zv - p.z) * zn) > 0
  }

  def torusGridPloygons(steps: Int) = {
    for {
      i <- 0 until steps
      j <- 0 until steps
    } yield {
      val poly = new Polygon()
      poly.add(torusPoint(i.toDouble / steps, j.toDouble / steps))
      poly.add(torusPoint((i + 1).toDouble / steps, j.toDouble / steps))
      poly.add(torusPoint((i + 1).toDouble / steps, (j + 1).toDouble / steps))
      poly.add(torusPoint(i.toDouble / steps, (j + 1).toDouble / steps))
      poly.setColor(Color.CYAN)
      // if (i % 5 != 0 || j % 5 != 0)
      poly.setFaceDisplayed(false)
      poly.setWireframeColor(org.jzy3d.colors.Color.GREEN);
      poly
    }
  }

  def line(xs: Int, ys: Int, steps: Int) = {
    val parameters =
      (0 to steps).map(i => (xs * i.toDouble / steps, ys * i.toDouble / steps))

    val points = parameters.zipWithIndex.map {
      case ((u, v), i) =>
        val p = torusPoint(u, v)
        if (isVisible(u, v) || i % 3 == 0)
          p.setColor(Color.MAGENTA)
        else p.setColor(Color.WHITE)
        p
    }
    val l = new LineStrip()
    points.foreach(
      l.add(_)
    )
    l.setWidth(3)
    l
  }

  def torusGridSurface(steps: Int): Shape = {
    val surf = new Shape(torusGridPloygons(steps).asJava)
    surf.setWireframeDisplayed(true);
    surf
  }

  def torusChart(steps: Int): Chart = {
    val chart = new AWTChart(Quality.Advanced)
    chart.add(torusGridSurface(steps))
    chart.add(line(2, 1, 25 * steps))
    // chart.add(SquareGrid.shape)
    chart
  }

  def torusImage(steps: Int): BufferedImage = {
    val tc = Torus.torusChart(steps)
    tc.setViewPoint(new Coord3d(xv, yv, zv))
    tc.open("Jzy3d Demo", 600, 600)
    import java.io._
    val imageFile = new File("image.png")
    tc.screenshot(imageFile)
    val image = ImageIO.read(imageFile)
    image
  }
}

object TorusApp extends App {
  Torus.torusImage(50)

}
