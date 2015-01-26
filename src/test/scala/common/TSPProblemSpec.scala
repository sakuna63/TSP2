package common

import java.io.File

import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by sakuna63 on 1/26/15.
 */
class TSPProblemSpec extends FlatSpec with Matchers {

  val fixture = new {
    val files = {
      val directory = new File("./samples")
      directory.listFiles()
    }
    val problems = files.map(new TSPProblem(_))
  }

  "A name property" should "be equal to filename without exp" in {
    val f = fixture
    f.files.zip(f.problems).foreach(pair => {
      var name = pair._1.getName
      name = name.substring(0, name.lastIndexOf('.'))

      pair._2.name should be (name)
    })
  }

  "Top 10 city's coordinate property" should "be equal to samples" in {
    val f = fixture
    val samples = Array(
      (37, 52),
      (49, 49),
      (52, 64),
      (20, 26),
      (40, 30),
      (21, 47),
      (17, 63),
      (31, 62),
      (52, 33),
      (51, 21)
    )
    val problem = f.problems.filter(_.name == "eil51")(0)

    problem.cities.take(10).zip(samples).foreach(pair => {
      val city = pair._1
      val sample = pair._2

      assert(city == sample)
    })
  }
}
