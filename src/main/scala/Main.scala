import java.io.File
import java.util.concurrent.CountDownLatch

import com.github.tototoshi.csv.CSVWriter
import common.{Calc, TSPProblem}
import generator.NNGenerator
import solver.{MMAntColonySolver, AntColonySolver, TwoOptSolver}
import scala.concurrent._
import scala.concurrent.duration.Duration
import ExecutionContext.Implicits.global
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
  val SEEDS = Array[Long](113, 367, 647, 947, 491, 997, 839, 1733, 271, 569)

  def main(args: Array[String]): Unit = {
    val directory = new File("./samples")

    val eil51 = new File(directory, "eil51.tsp")
    val problem = new TSPProblem(eil51)
    val solver = new MMAntColonySolver(problem.cities.length, ALPHA, _:Int, RHO, P_BEST, SEEDS(0))
    var betaResults = List[(Int, Int)]()
    val futures = for (beta <- BETA) yield
      Future {
        (beta, Calc.adjacentDis(problem, solver(beta).solve(problem)))
      }
    futures.foreach(f => betaResults = betaResults :+ Await.result(f, Duration.Inf))
    write("beta.csv", betaResults.map(_.productIterator.toSeq))

    val optimalBeta = betaResults.minBy(_._2)._1

    println(s"optimal beta is $optimalBeta")

    val results = for (f <- directory.listFiles()) yield {
      def average(distances: Array[Int]) = distances.sum.toDouble / distances.size
      def standardDeviation(distances: Array[Int]) = {
        val ave = average(distances)
        math.sqrt(distances.map(_ - ave).map(math.pow(_, 2)).sum / distances.size)
      }

      val problem = new TSPProblem(f)
      val solver = new MMAntColonySolver(problem.cities.length, ALPHA, optimalBeta, RHO, P_BEST, _:Long)

      var result = List[(Int, Long)]()
      val futures = for (seed <- SEEDS) yield
        Future {
          (Calc.adjacentDis(problem, solver(seed).solve(problem)), seed)
        }
      futures.foreach(f => result = result :+ Await.result(f, Duration.Inf))

      val optimalSeed = result.minBy(_._1)._2
      val distances = result.map(_._1).toArray

      (problem.name, optimalSeed, distances.max, distances.min, average(distances), standardDeviation(distances))
    }

    write("result.csv", results.map(_.productIterator.toSeq))
  }

  def write(name:String, result:Seq[Seq[Any]]) = {
    val writer = CSVWriter.open(name)
    writer.writeAll(result)
    writer.close()
  }
}
