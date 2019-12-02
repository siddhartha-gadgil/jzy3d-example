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

// object BuildSurfaceDemo extends App {

//     BuildSurface.chart.open("Jzy3d Demo", 600, 600)
  
//   }
  

object BuildSurface {
  val distDataProp =
    Vector(
      Vector(.25, .45, .20),
      Vector(.56, .89, .45),
      Vector(.6, .3, .7),
    )
  lazy val polygons =
    for {
      i <- 0 until distDataProp.length - 1
      j <- 0 until distDataProp.length - 1
    } yield {
      val polygon = new Polygon()
      polygon.add(new Point(new Coord3d(i, j, distDataProp(i)(j))));
      polygon.add(new Point(new Coord3d(i, j + 1, distDataProp(i)(j + 1))));
      polygon.add(
        new Point(new Coord3d(i + 1, j + 1, distDataProp(i + 1)(j + 1)))
      );
      polygon.add(new Point(new Coord3d(i + 1, j, distDataProp(i + 1)(j))));
    //   if (i +j % 2 == 1) polygon.setFaceDisplayed(false)
      polygon
    }

  lazy val polygonsFlip =
    for {
      i <- 0 until distDataProp.length - 1
      j <- 0 until distDataProp.length - 1
    } yield {
      val polygon = new Polygon()
      polygon.add(new Point(new Coord3d(i, j, - distDataProp(i)(j))));
      polygon.add(new Point(new Coord3d(i, j + 1, - distDataProp(i)(j + 1))));
      polygon.add(
        new Point(new Coord3d(i + 1, j + 1, - distDataProp(i + 1)(j + 1)))
      );
      polygon.add(new Point(new Coord3d(i + 1, j, - distDataProp(i + 1)(j))));
      polygon
    }

    lazy val line ={
        val p = new Point(new Coord3d(1, 1, 0.5))
        p.setColor(org.jzy3d.colors.Color.RED)
        val p2 = new Point(new Coord3d(2, 2, 1))
        p2.setColor(org.jzy3d.colors.Color.BLUE)
        val l = new LineStrip(p, p2 )
        l.setWidth(4)
        l.add(new Point(new Coord3d(3, 3, 1)))
        l.add(new Point(new Coord3d(1, 2, 0.5)))
        l.add(new Point(new Coord3d(0.5, 0.5, -0.5)))
        l
    } 

  lazy val surface = {
    val surf = (new Shape((polygons ++ polygonsFlip).asJava))
    surf.add(line)

    surf.setColorMapper(
      new ColorMapper(
        new ColorMapRainbow(),
        surf.getBounds().getZmin(),
        surf.getBounds().getZmax(),
        new org.jzy3d.colors.Color(1, 1, 1, 1f)
      )
    );
    surf.setWireframeDisplayed(true);
    surf.setWireframeColor(org.jzy3d.colors.Color.BLACK);
    line.setWireframeColor(org.jzy3d.colors.Color.MAGENTA)
    surf
  }
  lazy val chart = {
    val ch = new AWTChart(Quality.Advanced)
    ch.getScene().getGraph().add(surface);
    ch
  }

}
