package surface

import SquareGrid._
import org.jzy3d.plot3d.primitives.Shape
import scala.jdk.CollectionConverters._
import org.jzy3d.colors.Color

class SurfacePlot(n: Int) {
  def x(n: Int) = {
    val r = n % 2
    val q = (n - r) / 2
    (2 * q) + (r * 0.5)
  }

  val y: Map[Int, Double] = Map(0 -> 0, 1 -> 0.2, 2 -> 0.8, 3 -> 1)

  def z(n: Int) = n * 0.3

  def pointVector(a: Int, b: Int, c: Int) = PointVector(x(a), y(b), z(c))

  val grey = new Color(0.95f, 0.95f, 0.95f)

  val outerRows =
    (0 to 2 * n).toVector.flatMap { i =>
      val topBottom =
        for {
          k <- 0 to 1
          j <- 0 to 1
        } yield Square(
          pointVector(i, j * 2, k),
          pointVector(i + 1, j * 2, k),
          pointVector(i, j * 2 + 1, k),
          pointVector(i + 1, j * 2 + 1, k),
          colour = grey
        )
      val sides = for {
        j <- 0 to 1
      } yield Square(
        pointVector(i, j * 3, 0),
        pointVector(i + 1, j * 3, 0),
        pointVector(i, j * 3, 1),
        pointVector(i + 1, j * 3, 1),
        colour = Color.CYAN
      )
      (topBottom ++ sides).toVector
    }
  val holes =
    (0 until n).toVector.flatMap { m =>
      val horizontal =
        for {
          i <- 0 to 1
        } yield Square(
          pointVector(2 * m + 1 + i, 1, 0),
          pointVector(2 * m + 1 + i, 2, 0),
          pointVector(2 * m + 1 + i, 1, 1),
          pointVector(2 * m + 1 + i, 2, 1),
          colour = Color.CYAN
        )
      val vertical =
        for {
          j <- 1 to 2
        } yield Square(
          pointVector(2 * m + 1, j, 0),
          pointVector(2 * m + 2, j, 0),
          pointVector(2 * m + 1, j, 1),
          pointVector(2 * m + 2, j, 1),
          colour = Color.CYAN
        )
      horizontal ++ vertical
    }

  val middleRow =
    for {
      i <- 0 to n
      k <- 0 to 1
    } yield Square(
      pointVector((2 * i), 1, k),
      pointVector((2 * i) + 1, 1, k),
      pointVector((2 * i), 2, k),
      pointVector((2 * i) + 1, 2, k),
      colour = grey
    )

  val caps =
    for {
      i <- 0 to 1
      j <- 0 to 2
    } yield Square(
      pointVector(i * (2 * n + 1), j, 0),
      pointVector(i * (2 * n + 1), j + 1, 0),
      pointVector(i * (2 * n + 1), j, 1),
      pointVector(i * (2 * n + 1), j + 1, 1),
      colour = Color.CYAN
    )

  val allSquares =
    outerRows ++
      holes ++ caps ++
      middleRow
}

object SurfacePlot {
  val base = (new SurfacePlot(3)).allSquares

  val squares = base.flatMap(_.split(1))

  lazy val shape: Shape = {
    val surf = new Shape(squares.map(_.polygon).asJava)
    surf.setWireframeDisplayed(true);
    surf
  }
}
