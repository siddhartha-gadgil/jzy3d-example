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

import scala.math.{sin, cos, Pi}
import org.jzy3d.colors.colormaps.ColorMapHotCold
import org.jzy3d.colors.Color
import scala.util._

// import scala.collection.JavaConversions._

object Torus{
    def point(x: Double, y: Double, z: Double) : Point = new Point(new Coord3d(x, y, z))

    def torusPoint(u: Double, v: Double) : Point =
        {
            val z = cos(v * 2 * Pi)
            val x = sin(2 * Pi * u) * (5 + sin(2 * Pi * v))
            val y = cos(2 * Pi * u) * (5 + sin(2 * Pi * v))
            point(x, y, z)
        }

    def torusCoords(u: Double, v: Double) : Coord3d =
        {
            val z = cos(v * 2 * Pi)
            val x = sin(2 * Pi * u) * (5 + sin(2 * Pi * v))
            val y = cos(2 * Pi * u) * (5 + sin(2 * Pi * v))
            new Coord3d(x, y, z)
        }

    def torusGridPloygons(steps: Int) = {
        for {
            i <- 0 until steps 
            j <- 0 until steps
        }  yield {
            val poly = new Polygon()
            poly.add(torusPoint(i.toDouble/ steps, j.toDouble / steps))
            poly.add(torusPoint((i + 1).toDouble/ steps, j.toDouble / steps))
            poly.add(torusPoint((i + 1).toDouble/ steps, (j + 1).toDouble / steps))
            poly.add(torusPoint(i.toDouble/ steps, (j + 1).toDouble / steps))            
            if  ((i + j) % 2 != 0)
             poly.setFaceDisplayed(false)
            poly.setWireframeColor(org.jzy3d.colors.Color.GREEN);
            poly
        }
    }

    def line(xs: Int, ys: Int, steps: Int) = 
        {
            val points = (0 to steps).map(i => torusPoint(xs * i.toDouble / steps, ys * i.toDouble/steps)) 
            val l = new LineStrip()
            points.zipWithIndex.foreach{case (p, n) => 
                if (n % 2 == 1) p.setColor(Color.RED) else p.setColor(Color.BLUE)
                l.add(p)
            }
            l.setWidth(4)
            // l.setWireframeColor(Color.BLACK)
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
          surf.add(line(3, 2, steps))
          surf
    }

    def torusChart(steps: Int) = {
        val chart = new AWTChart(Quality.Advanced)
        chart.getScene().getGraph().add(torusGridSurface(steps));
        chart
    }
}

object TorusApp extends App {
    Torus.torusChart(60).open("Jzy3d Demo", 600, 600)
}