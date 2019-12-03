package surface

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
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

// import scala.collection.JavaConversions._

object Torus {
  def point(x: Double, y: Double, z: Double): Point =
    new Point(new Coord3d(x, y, z))

  def torusPoint(u: Double, v: Double): Point = {
    val z = cos(v * 2 * Pi)
    val x = sin(2 * Pi * u) * (5 + sin(2 * Pi * v))
    val y = cos(2 * Pi * u) * (5 + sin(2 * Pi * v))
    point(x, y, z)
  }

  def torusCoords(u: Double, v: Double): Coord3d = {
    val z = cos(v * 2 * Pi)
    val x = sin(2 * Pi * u) * (5 + sin(2 * Pi * v))
    val y = cos(2 * Pi * u) * (5 + sin(2 * Pi * v))
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
      poly.setFaceDisplayed(false)
      poly.setWireframeColor(org.jzy3d.colors.Color.GREEN);
      poly
    }
  }

  def line(xs: Int, ys: Int, steps: Int) = {

    val points = (0 to steps).map(
      i => torusPoint(xs * i.toDouble / steps, ys * i.toDouble / steps)
    )
    val l = new LineStrip(points.map(_.getCoord()).asJava)
    l.setWidth(3)
    l.setWireframeColor(Color.MAGENTA)
    l
  }

  def torusGridSurface(steps: Int) = {
    val surf = new Shape(torusGridPloygons(steps).asJava)
    surf.setColorMapper(
      new ColorMapper(
        new ColorMapRainbow(),
        surf.getBounds().getZmin(),
        surf.getBounds().getZmax(),
        new org.jzy3d.colors.Color(1, 1, 1, 1f)
      )
    );
    surf.setWireframeDisplayed(true);
    surf
  }

  def torusChart(steps: Int) = {
    val chart = new AWTChart(Quality.Advanced)
    chart.getScene().getGraph().add(torusGridSurface(steps))
    chart.getScene().getGraph().add(line(2, 1, steps));
    chart
  }

  def torusImage(steps: Int) = {
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
