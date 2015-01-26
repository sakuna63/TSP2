package solver

import java.io.File

import common.{Calc, TSPProblem}
import generator.NNGenerator
import org.scalatest._

/**
 * Created by sakuna63 on 12/25/14.
 */
class TwoOptSpec extends FlatSpec with Matchers {

  val fixture = new {
    val files = {
      val directory = new File("./samples")
      directory.listFiles()
    }
    val problems = files.map(new TSPProblem(_))
  }

  "A result's values" should "be unique for all problem" in {
    fixture.problems.foreach(problem => {
      val nn = NNGenerator.adjacent(problem, 0)
      println(Calc.adjacentDis(problem.distance, nn))
      val result = new TwoOptSolver(nn).solve(problem)
      println(result.mkString(","))
      println(Calc.adjacentDis(problem.distance, result))

      result.distinct.size should be (result.size)
    })
  }

  "A result's values" should "not loop" in {
    fixture.problems.foreach(p => {
      val nn = NNGenerator.adjacent(p, 0)
      val path = new TwoOptSolver(nn).solve(p)
      var at = 0
      for (i <- 1 to p.cities.length) {
        at = path(at)
      }
      at should be (0)
    })
  }
}
