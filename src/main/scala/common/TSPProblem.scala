package common

import java.io.File

import scala.io.Source

/**
 * Created by sakuna63 on 12/23/14.
 */
class TSPProblem(file: File) {
  val name: String = {
    val line = Source.fromFile(file).getLines().next()
    line.substring(line.indexOf(':') + 2)
  }
  val cities: Array[(Double, Double)] = {
    var cities: List[(Double, Double)] = List()
    def correctLineFormat(line: String): Boolean = line != "EOF" && line.length > 0
    Source.fromFile(file).getLines()
      .drop(6)
      .filter(correctLineFormat)
      .foreach( l => {
        val line: Array[String] = l.split(" ").filter(!_.isEmpty)
        cities = (line(1).toDouble, line(2).toDouble) :: cities
      })
    cities.toArray
  }
  val distance: Array[Array[Double]] = {
    val distance = new Array[Array[Double]](cities.length)
    cities.zipWithIndex.foreach({ case ((x1, y1), i) =>
      distance(i) = new Array[Double](cities.length)
      cities.zipWithIndex.foreach({ case ((x2, y2), j) =>
        distance(i)(j) = if (i == j) -1 else math.sqrt(math.pow(x1 - x2, 2) + math.pow(y1 - y2, 2))
      })
    })
    distance
  }
}
