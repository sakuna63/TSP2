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
        var l1 = i2
        for (l <- 0 until path.length - 2) {
          l1 = path(l1)
          val l2 = path(l1)
          if (i1 != l2 && l1 != i2) {
            val disOld1 = problem.distance(i1)(i2)
            val disOld2 = problem.distance(l1)(l2)
            val disNew1 = problem.distance(i1)(l2)
            val disNew2 = problem.distance(l1)(i2)
            if (disOld1 + disOld2 > disNew1 + disNew2) {
              path(i1) = l2
              path(l1) = i2
              count = count + 1
            }
          }
        }
      }
    }
    path
  }
}
