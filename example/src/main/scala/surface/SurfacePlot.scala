package surface

import SquareGrid._
import org.jzy3d.plot3d.primitives.Shape
import scala.jdk.CollectionConverters._
import org.jzy3d.colors.Color

class SurfacePlot(n: Int) {
  val outerRows =
    (0 to 2 * n).toVector.flatMap { i =>
      val topBottom =
        for {
          k <- 0 to 1
          j <- 0 to 1
        } yield Square(
          PointVector(i, j * 2, k),
          PointVector(i + 1, j * 2, k),
          PointVector(i, j * 2 + 1, k),
          PointVector(i + 1, j * 2 + 1, k)
        )
      val sides = for {
        j <- 0 to 1
      } yield Square(
        PointVector(i, j * 3, 0),
        PointVector(i + 1, j * 3, 0),
        PointVector(i, j * 3, 1),
        PointVector(i + 1, j * 3, 1)
      )
      (topBottom ++ sides).toVector
    }
  val holes =
    (0 until n).toVector.flatMap { m =>
      val horizontal =
        for {
          i <- 0 to 1
        } yield Square(
          PointVector(2 * m + 1 + i, 1, 0),
          PointVector(2 * m + 1 + i, 2, 0),
          PointVector(2 * m + 1 + i, 1, 1),
          PointVector(2 * m + 1 + i, 2, 1)
        )
      val vertical =
        for {
          j <- 1 to 2
        } yield Square(
          PointVector(2 * m + 1, j, 0),
          PointVector(2 * m + 2, j, 0),
          PointVector(2 * m + 1, j, 1),
          PointVector(2 * m + 2, j, 1)
        )
      horizontal ++ vertical
    }

  val middleRow =
    for {
      i <- 0 to n
      k <- 0 to 1
    } yield Square(
      PointVector((2 * i), 1, k),
      PointVector((2 * i) + 1, 1, k),
      PointVector((2 * i), 2, k),
      PointVector((2 * i) + 1, 2, k)
    )

  val caps =
    for {
      i <- 0 to 1
      j <- 0 to 2
    } yield Square(
      PointVector(i * (2 * n + 1), j, 0),
      PointVector(i * (2 * n + 1), j + 1, 0),
      PointVector(i * (2 * n + 1), j, 1),
      PointVector(i * (2 * n + 1), j + 1, 1)
    )

  val allSquares =
    outerRows ++
      holes ++ caps ++
      middleRow
}

object SurfacePlot {
  val base = (new SurfacePlot(5)).allSquares

  val squares = base.flatMap(_.split(2))

  lazy val shape: Shape = {
    val surf = new Shape(squares.map(_.polygon).asJava)
    surf.setWireframeDisplayed(true);
    surf
  }
}
