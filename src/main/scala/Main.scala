import java.io.File
import java.util.concurrent.CountDownLatch

import com.github.tototoshi.csv.CSVWriter
import common.{Calc, TSPProblem}
import generator.NNGenerator
import solver.{MMAntColonySolver, AntColonySolver, TwoOptSolver}
import scala.runtime.ScalaRunTime._

import scala.io.Source
import scala.util.Random

/**
 * Created by sakuna63 on 12/23/14.
 */
object Main {
  val ALPHA = 1
  val BETA = Array(2,3,4,5)
  val RHO = 0.2
  val P_BEST = 0.05
  val SEEDS = Array(113, 367, 647, 947, 491, 997, 839, 1733, 271, 569)

  def main(args: Array[String]): Unit = {
    val directory = new File("./samples")

    val eil51 = new File(directory, "eil51.tsp")
    val problem = new TSPProblem(eil51)
    val solver = createSolver(problem.cities.length, ALPHA, RHO, P_BEST)(SEEDS(0))
    val optimalBeta = SEEDS.map(beta => (Calc.adjacentDis(problem, solver(beta).solve(problem)), beta)).minBy(_._1)._2

//    val results = directory.listFiles().map(f => {
    val results = for (f <- directory.listFiles()) yield {
      def average(distances: Array[Double]) = distances.sum / distances.size
      def standardDeviation(distances: Array[Double]) = {
        val ave = average(distances)
        math.sqrt(distances.map(_ - ave).map(math.pow(_, 2)).sum / distances.size)
      }

      val problem = new TSPProblem(f)
      val solver = createSolver(problem.cities.length, ALPHA, RHO, P_BEST)(_)(beta = optimalBeta)

      val result = for (seed <- SEEDS) yield (Calc.adjacentDis(problem, solver(seed).solve(problem)), seed)
      val optimalSeed = result.minBy(_._1)._2
      val distances = result.map(_._1).toArray

      (problem.name, optimalSeed, distances.max, distances.min, average(distances), standardDeviation(distances))
    }

    val writer = CSVWriter.open("results.csv")
    writer.writeAll(results.map(_.productIterator.toSeq))
    writer.close()
  }

  def createSolver(m:Int, alpha:Int, rho:Double, bestP:Double)(seed:Long)(beta:Int) = {
    new MMAntColonySolver(m, alpha, beta, rho, bestP, seed)
  }
}
