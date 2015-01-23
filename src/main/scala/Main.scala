import java.io.File

import com.github.tototoshi.csv.CSVWriter
import common.{Calc, TSPProblem}
import solver.HybridACOSolver
import scala.concurrent._
import scala.concurrent.duration.Duration
import ExecutionContext.Implicits.global
import scala.util.Random

/**
 * Created by sakuna63 on 12/23/14.
 */
object Main {
  val M = 25
  val ALPHA = 1
  val BETA = 2
  val RHO = Array[Double](0.01, 0.02, 0.1, 0.2, 0.5, 0.8)
  val P_BEST = Array[Double](0.001, 0.005, 0.01, 0.05, 0.1, 0.5)
  val SEED = 113
  val RND = new Random(SEED)
  val LOOP = 200

  def main(args: Array[String]): Unit = {
    val directory = new File("./samples")

    val results = for (f <- directory.listFiles()) yield {
      def average(distances: Array[Int]) = distances.sum.toDouble / distances.size
      def standardDeviation(distances: Array[Int]) = {
        val ave = average(distances)
        math.sqrt(distances.map(_ - ave).map(math.pow(_, 2)).sum / distances.size)
      }

      val problem = new TSPProblem(f)
      val const = new HybridACOSolver(M, ALPHA, BETA, _:Double, _:Double, RND, LOOP)

      val futures =
        for (i <- 1 to 10) yield
          for (rho <- RHO) yield
            for (bestP <- P_BEST) yield
              Future {
                val solver = const(rho, bestP)
                (solver, Calc.adjacentDis(problem, solver.solve(problem)))
              }

      var result = List[(HybridACOSolver, Int)]()
      futures.flatten.flatten.foreach(f => result = result :+ Await.result(f, Duration.Inf))

      val optimalHash = result.minBy(_._2)._1.hashCode()
      val optimalRho = result.minBy(_._2)._1.rho
      val optimalP = result.minBy(_._2)._1.bestP
      val distances = result.map(_._2).toArray

      (problem.name, optimalHash, optimalRho, optimalP, distances.max, distances.min, average(distances), standardDeviation(distances))
    }

   write("result.csv", results.map(_.productIterator.toSeq))
  }

  def write(name:String, result:Seq[Seq[Any]]) = {
    val writer = CSVWriter.open(name)
    writer.writeAll(result)
    writer.close()
  }
}
