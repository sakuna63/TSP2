package solver

import java.io.File

import common.TSPProblem
import org.scalatest._

import scala.util.Random

/**
 * Created by sakuna63 on 1/25/15.
 */
class HybridACOSpec extends FlatSpec with Matchers {

  val M = 25
  val ALPHA = 1
  val BETA = 2
  val RHO = Array[Double](0.01, 0.02, 0.1, 0.2, 0.5, 0.8)
  val P_BEST = Array[Double](0.001, 0.005, 0.01, 0.05, 0.1, 0.5)
  val SEED = 113
  val RND = new Random(SEED)
  val LOOP = 200

  def fixture = new {
    val file = new File("./samples/eil51.tsp")
    val problem = new TSPProblem(file)
    val cityNum = problem.cities.length
    val solver = new HybridACOSolver(M, ALPHA, BETA, RHO(0), P_BEST(0), new Random(SEED), LOOP)
  }

  "A result array length" should "be equal to (city num - selected city num)" in {
    val f = fixture
    val path = f.solver.solve(f.problem)
    path.distinct.length should be (path.length)
  }

}
