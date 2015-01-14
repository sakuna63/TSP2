package solver

import common.{Calc, TSPProblem}
import generator.NNGenerator

import scala.collection.immutable.IndexedSeq

/**
 * Created by sakuna63 on 1/14/15.
 */
class MMAntColonySolver(m:Int, alpha:Int, beta:Int, rho:Double, bestP:Double, seed:Long)
  extends AntColonySolver(m:Int, alpha:Int, beta:Int, rho:Double, seed:Long) {

  def refreshPheromone(pheromones: Array[Array[Double]],
                       pathAndDistances: IndexedSeq[(Array[Int], Double)],
                       bestSolution: (Array[Int], Double)): Array[Array[Double]] = {

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
    println(s"${problem.name}-$seed")

    var pheromones = initPheromones(problem)
    var bestSolution: (Array[Int], Double) = null
    var distances = List[Double]()
    var count = 0

    while (count < 1000) {
      val ants = for(i <- 1 to m) yield createAnt(pheromones, problem)
      pheromones = refreshPheromone(pheromones, ants, bestSolution)

      val solution = ants.minBy(_._2)
      if (bestSolution == null || solution._2 < bestSolution._2) {
        bestSolution = solution
        count = 0
      }
      else {
        count += 1
      }
      distances = distances :+ bestSolution._2
    }

    outputCSV(problem, distances)
    bestSolution._1
  }
}
