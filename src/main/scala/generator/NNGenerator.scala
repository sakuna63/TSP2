package generator

import common.TSPProblem

/**
 * Created by sakuna63 on 12/25/14.
 */
object NNGenerator extends Generator {
  override def path(problem: TSPProblem, start: Int): Array[Int] = {
    var path = List(start)
    var base = start
    1 until problem.cities.length foreach (_ => {
      base = problem.distance(base)
        .zipWithIndex
        .filter({ case (dis, i) => !path.contains(i)})
        .minBy(_._1)
        ._2
      path = base :: path
    })
    path.toArray
  }

  override def order(problem: TSPProblem, start: Int): Array[Int] = {
    throw new UnsupportedOperationException
  }

  override def adjacent(problem: TSPProblem, start: Int): Array[Int] = {
    val path = Array.fill(problem.cities.length)(-1)
    var base = start
    1 until path.size foreach(_ => {
      path(base) = problem.distance(base)
        .zipWithIndex
        .filter(_._2 != base)                         // except row == column element
        .filter({case (dis, i) => i != start && !path.contains(i)}) // except city already used
        .minBy(_._1)
        ._2
      base = path(base)
    })
    path(base) = start
    path
  }
}
