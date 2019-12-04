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
  def point(x: Double, y: Double, z: Double): Point =
    new Point(new Coord3d(x, y, z))

  def torusPoint(u: Double, v: Double, sc: Double = 1): Point = {
    val z = cos(v * 2 * Pi)
    val x = sin(2 * Pi * u) * (5 + sin(2 * Pi * v))
    val y = cos(2 * Pi * u) * (5 + sin(2 * Pi * v))
    point(x, y, z)
  }

  def torusCoords(u: Double, v: Double, r: Double = 1): Coord3d = {
    val z = r * cos(v * 2 * Pi)
    val x = sin(2 * Pi * u) * (5 + (r * sin(2 * Pi * v)))
    val y = cos(2 * Pi * u) * (5 + (r * sin(2 * Pi * v)))
    new Coord3d(x, y, z)
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

    val points = (0 to steps).map(
      i => torusPoint(xs * i.toDouble / steps, ys * i.toDouble / steps)
    )
    points.zipWithIndex.foreach {
      case (p, i) =>
        if (p.getCoord().z > 0 || i % 3 == 0) p.setColor(Color.MAGENTA)
        else p.setColor(Color.WHITE)
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
    chart
  }

  def torusImage(steps: Int): BufferedImage = {
    val tc = Torus.torusChart(steps)
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
