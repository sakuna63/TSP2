package generator

import java.io.File

import common.TSPProblem
import org.scalatest._

/**
 * Created by sakuna63 on 12/25/14.
 */
class NNSpec extends FlatSpec with Matchers {

  "A result's values" should "be unique for all problem" in {
    val directory = new File("./samples")
    directory.listFiles().foreach(f => {
      val problem = new TSPProblem(f)
      val result = NNGenerator.adjacent(problem, 0)

      result.distinct.size should be (result.size)
    })
  }

  "A result's values" should "not be include -1 for all problem" in {
    val directory = new File("./samples")
    directory.listFiles().foreach(f => {
      val problem = new TSPProblem(f)
      val result = NNGenerator.adjacent(problem, 0)

      result.contains(-1) should be (false)
    })
  }

  "A result's values" should "be available to back to start by following path" in {
    val directory = new File("./samples")
    directory.listFiles().foreach(f => {
      val start = 0
      val problem = new TSPProblem(f)
      val result = NNGenerator.adjacent(problem, start)

      var at = start
      var at_history = List(start)
      0 until result.size foreach(i => {
        at = result(at)
        at_history = at :: at_history
      })

      at should be (start)
      at_history.distinct.size should be (result.size)
    })
  }
}
