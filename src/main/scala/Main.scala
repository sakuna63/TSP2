import java.io.File
import java.util.concurrent.CountDownLatch

import com.github.tototoshi.csv.CSVWriter
import common.{Calc, TSPProblem}
import generator.NNGenerator
import solver.{AntColonySolver, TwoOptSolver}
import scala.runtime.ScalaRunTime._

import scala.io.Source
import scala.util.Random

/**
 * Created by sakuna63 on 12/23/14.
 */
object Main {
  val SEEDS = Array(113, 367, 647, 947, 491, 997, 839, 1733, 271, 569)

  def main(args: Array[String]): Unit = {
    val directory = new File("./samples")
    directory.listFiles().foreach(f => {
      val problem = new TSPProblem(f)
      println(problem.name)

      def average(distances: Array[Double]) = distances.sum / distances.size
      def standardDeviation(distances: Array[Double]) = {
        val ave = average(distances)
        math.sqrt(distances.map(_-ave).map(math.pow(_, 2)).sum / distances.size)
      }

      val m = problem.cities.length
      val alpha = 1
      val beta = 2
      val rho = 0.5

      new Thread(new Runnable {
        override def run(): Unit = {
          val distances = for (seed <- SEEDS) yield (seed, new AntColonySolver(m, alpha, beta, rho, seed).solve(problem))
          println(s"${problem.name}-${distances.minBy({case (seed, path) => Calc.adjacentDis(problem, path)})._1}")
        }
      }).start()

//      (problem.name, distances.max, distances.min, average(distances), standardDeviation(distances))
    })

//    val writer = CSVWriter.open("results.csv")
//    writer.writeAll(results.map(_.productIterator.toSeq))
//    writer.close()
  }
}
