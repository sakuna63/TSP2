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

//  "A name property" should "be equal to filename without exp" in {
//    val f = fixture
//    f.files.zip(f.problems).foreach(pair => {
//      var name = pair._1.getName
//      name = name.substring(0, name.lastIndexOf('.'))
//
//      pair._2.name should be (name)
//    })
//  }
//
//  "Top 10 city's coordinate property" should "be equal to samples" in {
//    val f = fixture
//    val samples = Array(
//      (37, 52),
//      (49, 49),
//      (52, 64),
//      (20, 26),
//      (40, 30),
//      (21, 47),
//      (17, 63),
//      (31, 62),
//      (52, 33),
//      (51, 21)
//    )
//    val problem = f.problems.filter(_.name == "eil51")(0)
//
//    problem.cities.take(10).zip(samples).foreach(pair => {
//      val city = pair._1
//      val sample = pair._2
//
//      assert(city == sample)
//    })
//  }
//
//  "A same index distance" should "be equal to -1" in {
//    val f = fixture
//    f.problems.foreach(p => {
//      for(i <- 0 until p.cities.length) {
//        assert(p.distance(i)(i) == -1)
//      }
//    })
//  }

  "test" should "" in {
    val path = Array(21,28,19,46,48,47,22,25,49,38,37,45,17,24,43,1,36,3,40,34,33,0,6,42,13,7,31,30,20,9,27,10,44,29,35,2,16,4,32,18,12,39,23,41,14,50,11,5,8,15,26)
    var at = 0
    for (i <- 1 to 51) {
      println(at)
      at = path(at)
    }
    assert(at == 0)
//    val problem = fixture.problems.filter(_.name == "eil51")(0)
//
//    println(Calc.adjacentDis(problem.distance, path))
  }
}
