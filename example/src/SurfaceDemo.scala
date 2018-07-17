package org.jzy3d.demos.surface;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.chart._


object SurfaceDemo extends App{
    plot()

    def plot() : Unit = {
        // Define a function to plot
        val mapper = new Mapper() {
          def f(x: Double, y:  Double) =
              10 * Math.sin(x / 10) * Math.cos(y / 20);
          }

        // Define range and precision for the function to plot
        val range = new Range(-150, 150);
        val steps = 50;

        // Create a surface drawing that function
        val surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.BLACK);

        // Create a chart and add the surface
        val chart = new AWTChart(Quality.Advanced);
        chart.add(surface);
        chart.open("Jzy3d Demo", 600, 600);
      }
}
