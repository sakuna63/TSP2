package solver

import java.io.File

import common.TSPProblem
import org.scalatest._

/**
 * Created by sakuna63 on 1/9/15.
 */
class AntColonySpec extends FlatSpec with Matchers {

  def fixture = new {
    val file = new File("./samples/eil51.tsp")
    val problem = new TSPProblem(file)
    val cityNum = problem.cities.length
    val solver = new AntColonySolver(cityNum, 1, 2, 0.5, 113)
    val pheromones = Array.fill(cityNum, cityNum)(0.5)
    val distances = Array.fill(cityNum, cityNum)(10)
    val selectedCities = Array(1,2,3)
    val base = 3
  }

  "A result array length" should "be equal to (city num - selected city num)" in {
    val f = fixture
    val probabilities = f.solver.toProbabilities(f.pheromones(f.base), f.selectedCities, f.distances(f.base))

    probabilities.length should be (f.cityNum - f.selectedCities.length)
  }

  "A result" should "be not contains selectedCities" in {
    val f = fixture
    val probabilities = f.solver.toProbabilities(f.pheromones(f.base), f.selectedCities, f.distances(f.base))

    f.selectedCities foreach (city => {
      assert(!probabilities.map(_._2).contains(city))
    })
  }

  "A result" should "be not contains 0 pheromones" in {
    val f = fixture
    val probabilities = f.solver.toProbabilities(f.pheromones(f.base), f.selectedCities, f.distances(f.base))

    assert(!probabilities.map(_._1).contains(0))
  }

  "A result" should "be random" in {
    val f = fixture
    for (i <- f.cityNum / 2 to f.cityNum - 1) {
      f.pheromones(f.base)(i) = 10
    }
    for (i <- 1 to 10) {
      val route = f.solver.selectRoute(f.base, f.selectedCities, f.pheromones, f.distances)
      println(route)
    }
  }

  "A result's values" should "be unique for all problem" in {
    val directory = new File("./samples")
    directory.listFiles().foreach(f => {
      val problem = new TSPProblem(f)
      val cityNum = problem.cities.length
      val pheromones = Array.fill(cityNum, cityNum)(0.5)
      val result = new AntColonySolver(cityNum, 1, 2, 0.5, 113).createAnt(pheromones, problem)

      result._1.distinct.size should be (result._1.size)
    })
  }
}
