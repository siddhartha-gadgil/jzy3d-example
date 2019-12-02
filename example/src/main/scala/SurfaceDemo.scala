package surface

import org.jzy3d.analysis.AbstractAnalysis
import org.jzy3d.analysis.AnalysisLauncher
import org.jzy3d.chart.factories.AWTChartComponentFactory
import org.jzy3d.colors.Color
import org.jzy3d.colors.ColorMapper
import org.jzy3d.colors.colormaps.ColorMapRainbow
import org.jzy3d.maths.Range
import org.jzy3d.plot3d.builder.Builder
import org.jzy3d.plot3d.builder.Mapper
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid
import org.jzy3d.plot3d.primitives.Shape
import org.jzy3d.plot3d.rendering.canvas.Quality
import org.jzy3d.chart._
import org.jzy3d.plot3d.rendering.view.AWTRenderer3d

// object SurfaceDemo extends App {

//   Surface.chart.open("Jzy3d Demo", 600, 600)

// }

object Surface{
  // Define a function to plot
  lazy val mapper = new Mapper() {
    def f(x: Double, y: Double) =
      10 * Math.sin(x / 10) * Math.cos(y / 20)
  }

  lazy val mapper2 = new Mapper() {
    def f(x: Double, y: Double) =
      - 10 * Math.sin(x / 10) * Math.cos(y / 20)
  }

  // Define range and precision for the function to plot
  lazy val range = new Range(-150, 150)
  val steps = 50

  // Create a surface drawing that function
  lazy val surface = {
    val surf = Builder.buildOrthonormal(new OrthonormalGrid(range, steps), mapper)
    surf.setColorMapper(
    new ColorMapper(new ColorMapRainbow(),
                    surf.getBounds().getZmin(),
                    surf.getBounds().getZmax(),
                    new Color(1, 1, 1, .5f)))
    surf.setFaceDisplayed(true)
    surf.setWireframeDisplayed(false)
    surf.setWireframeColor(Color.BLACK)
    surf 
  }

  lazy val surface2 = {
    val surf = Builder.buildOrthonormal(new OrthonormalGrid(range, steps), mapper2)
    surf.setColorMapper(
    new ColorMapper(new ColorMapRainbow(),
                    surf.getBounds().getZmin(),
                    surf.getBounds().getZmax(),
                    new Color(1, 1, 1, .5f)))
    surf.setFaceDisplayed(true)
    surf.setWireframeDisplayed(false)
    surf.setWireframeColor(Color.BLACK)
    surf 
  }

  // Create a chart and add the surface
  lazy val chart = {
    val ach = new AWTChart(Quality.Advanced)
    ach.add(surface)
    ach.add(surface2)
    ach
  }

  lazy val quietChart = {
    val ch = AWTChartComponentFactory.chart(Quality.Advanced, "offscreen")
    ch.add(surface)
    ch
  }

  lazy val canvas = quietChart.getCanvas()

  lazy val buf = {
    val renderer = canvas.getRenderer().asInstanceOf[AWTRenderer3d]
    canvas.screenshot()
    renderer.getLastScreenshotImage()
  }
}