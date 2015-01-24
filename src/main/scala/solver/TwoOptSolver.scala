package solver

import common.TSPProblem

/**
 * Created by sakuna63 on 12/25/14.
 */
class TwoOptSolver(path: Array[Int]) extends Solver {
  override def solve(problem: TSPProblem): Array[Int] = {
    var i1 = -1
    var count = -1

    while(count != 0) {
      count = 0
      for (i <- 0 until path.length - 2) {
        i1 = if (i1 == -1) 0 else path(i1)
        val i2 = path(i1)
        val l = seekBestPathToExchange(problem, i1, i2)
        l foreach (l => {
          path(i1) = l._2
          path(l._1) = i2
          count += 1
        })
      }
    }
    path
  }

  def seekBestPathToExchange(problem: TSPProblem, i1: Int, i2: Int): Option[(Int, Int)] = {
    var l1 = i2
    var bestPathToExchange: ((Int, Int), Int) = (null, Integer.MAX_VALUE)

    for (l <- 0 until path.length - 2) {
      l1 = path(l1)
      val l2 = path(l1)
      if (i1 != l2 && l1 != i2) {
        val disOld1 = problem.distance(i1)(i2)
        val disOld2 = problem.distance(l1)(l2)
        val disNew1 = problem.distance(i1)(l2)
        val disNew2 = problem.distance(l1)(i2)
        if (disOld1 + disOld2 > disNew1 + disNew2 && bestPathToExchange._2 > disNew1 + disNew2) {
          bestPathToExchange = ((l1, l2), disNew1 + disNew2)
        }
      }
    }

    if (bestPathToExchange._1 == null) None
    else Some( bestPathToExchange._1 )
  }
}
