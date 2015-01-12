package solver

import common.TSPProblem

/**
 * Created by sakuna63 on 12/25/14.
 */
trait Solver {
  def solve(problem: TSPProblem): Array[Int]
}
