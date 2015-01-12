package solver

import com.github.tototoshi.csv.CSVWriter
import common.TSPProblem
import common.Calc
import generator.NNGenerator

import scala.collection.immutable.IndexedSeq
import scala.util.Random

/**
 * Created by sakuna63 on 1/7/15.
 */
class AntColonySolver(m:Int, alpha:Int, beta:Int, rho:Double, seed:Long) extends Solver {
  val rnd = new Random(seed)

  def toProbabilities(pheromones: Array[Double], selectedCities: Array[Int], distances: Array[Double]): Array[(Double, Int)] = {
    pheromones.zipWithIndex
      .filter({ case(p, i) => !selectedCities.contains(i)})
      .map({ case(p, i) =>
        val t = Math.pow(p, alpha)
        val n = Math.pow(1.0 / distances(i), beta)
        (t * n, i)
      })
  }

  def selectRoute(base: Int, selectedCities: Array[Int], pheromones: Array[Array[Double]], distances: Array[Array[Double]]): Int = {
    val probabilities = toProbabilities(pheromones(base), selectedCities, distances(base))
    val threshold = rnd.nextDouble() * probabilities.map(_._1).sum
    var tempSum = 0.0
    probabilities.foreach({case(p, i) =>
        tempSum += p
        if (tempSum >= threshold) {
          return i
        }
    })
    throw new IllegalArgumentException("fail")
  }

  def refreshPheromone(pheromones: Array[Array[Double]], pathAndDistances: IndexedSeq[(Array[Int], Double)]): Array[Array[Double]] = {
    pheromones.zipWithIndex.map({ case(arr, i) =>
      arr.zipWithIndex.map({ case(p, l) =>
        val sum = pathAndDistances
          .filter({case(path, dis) => path(i) == l || path(l) == i})
          .map(1.0 / _._2)
          .sum // ∑Δτ_ij
        (1 - rho) * p + sum
      })
    })
  }
  // a => {( == i && l1 == l) || (i1 == l && l1 == i)}


  override def solve(problem: TSPProblem): Array[Int] = {
    println(s"${problem.name}-$seed")
    val cityNum = problem.cities.length
    val sampleDistance = Calc.adjacentDis(problem, NNGenerator.adjacent(problem, 0))
    var pheromones: Array[Array[Double]] = Array.fill(cityNum, cityNum)(m.toDouble / sampleDistance)
    var bestSolution: (Array[Int], Double) = null
    var distances = List[Double]()
    var count = 0

    while (count < 1000) {
      val ants = for(i <- 1 to m) yield createAnt(pheromones, problem)
      pheromones = refreshPheromone(pheromones, ants)

      val solution = ants.minBy(_._2)
      if (bestSolution == null || solution._2 < bestSolution._2) {
        bestSolution = solution
        count = 0
      }
      else {
        count += 1
      }
      distances = distances :+ solution._2
    }

    val writer = CSVWriter.open(s"results-${problem.name}-$seed.csv")
    writer.writeRow((1 to distances.size).toSeq)
    writer.writeRow(distances.toSeq)
    writer.close()

    bestSolution._1
  }

  def createAnt(pheromones: Array[Array[Double]], problem: TSPProblem): (Array[Int], Double) = {
    val cityNum = problem.cities.length
    val path = Array.fill(cityNum)(-1)
    val start = rnd.nextInt(cityNum)
    var base = start
    1 until cityNum foreach (_ => {
      val city = selectRoute(base, path.filter(_ != -1) :+ start, pheromones, problem.distance)
      path(base) = city
      base = city
    })
    path(base) = start
    (path, Calc.adjacentDis(problem, path))
  }
}