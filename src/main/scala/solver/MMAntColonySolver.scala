package solver

import common.{Calc, TSPProblem}
import generator.NNGenerator

import scala.collection.immutable.IndexedSeq
import scala.util.Random

/**
 * Created by sakuna63 on 1/14/15.
 */
class MMAntColonySolver(m:Int, alpha:Int, beta:Int, rho:Double, val bestP:Double, rnd:Random,  loopCount:Int)
  extends AntColonySolver(m, alpha, beta, rho, rnd, loopCount) {

  def refreshPheromone(pheromones: Array[Array[Double]],
                       pathAndDistances: IndexedSeq[(Array[Int], Int)],
                       bestSolution: (Array[Int], Int)): Array[Array[Double]] = {

    def slash(value: Double, min: Double, max: Double) = if (value > max) max else if (value < min) min else value
    val n = pheromones.length
    val decP = math.pow(bestP, n)
    val maxT = 1.0 / rho * bestSolution._2
    val minT = (1 - decP) / (n/2 - 1) * decP * maxT
    val best = pathAndDistances.minBy(_._2)

    pheromones.zipWithIndex.map({ case(arr, i) =>
      arr.zipWithIndex.map({ case(p, l) =>
        // τ_ij_best
        val t: Double = if (best._1(i) == l || best._1(l) == i ) 1.0 / best._2 else 0.0
        slash((1 - rho) * p + t, minT, maxT)
      })
    })
  }

  override def initPheromones(problem: TSPProblem): Array[Array[Double]] = {
    val cityNum = problem.cities.length
    val sampleDistance = Calc.adjacentDis(problem, NNGenerator.adjacent(problem, 0))
    Array.fill(cityNum, cityNum)(m.toDouble / (rho * sampleDistance))
  }

  override def solve(problem: TSPProblem): Array[Int] = {
    println(s"${problem.name}-$hashCode()")

    var pheromones = initPheromones(problem)
    var bestSolution: (Array[Int], Int) = null
    var distances = List[Int]()
    var count = 0

    while (count < loopCount) {
      val ants = for(i <- 1 to m) yield {
        val path = createAntPath(pheromones, problem)
        (path, Calc.adjacentDis(problem, path))
      }

      val solution = ants.minBy(_._2)
      if (bestSolution == null || solution._2 < bestSolution._2) {
        bestSolution = solution
        count = 0
      }
      else {
        count += 1
      }
      distances = distances :+ bestSolution._2
      pheromones = refreshPheromone(pheromones, ants, bestSolution)
    }

    outputCSV(problem, distances)
    bestSolution._1
  }

  override def toString: String = super.toString
}
