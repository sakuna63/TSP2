package solver

import common.TSPProblem

/**
 * Created by sakuna63 on 1/23/15.
 */
class HybridACOSolver(m:Int, alpha:Int, beta:Int, rho:Double, bestP:Double, seed:Long, loopCount:Int)
  extends MMAntColonySolver(m, alpha, beta, rho, bestP, seed, loopCount) {

  override def createAntPath(pheromones: Array[Array[Double]], problem: TSPProblem): Array[Int] = {
    val path = super.createAntPath(pheromones, problem)
    new TwoOptSolver(path).solve(problem)
  }
}
