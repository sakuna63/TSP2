package solver

import java.io.File

import common.{Calc, TSPProblem}
import generator.NNGenerator
import org.scalatest._

/**
 * Created by sakuna63 on 12/25/14.
 */
class TwoOptSpec extends FlatSpec with Matchers {

//  "A UnsupportedOperationException" should "be throwed when "

  "A result's values" should "be unique for all problem" in {
    val directory = new File("./samples")
    directory.listFiles().foreach(f => {
      val problem = new TSPProblem(f)
      val nn = NNGenerator.adjacent(problem, 0)
      println(Calc.adjacentDis(problem.distance, nn))
      val result = new TwoOptSolver(nn).solve(problem)
      println(Calc.adjacentDis(problem.distance, result))

      result.distinct.size should be (result.size)
    })
  }

}
