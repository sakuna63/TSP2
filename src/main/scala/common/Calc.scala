package common

/**
 * Created by sakuna63 on 1/7/15.
 */
object Calc {
  def pathDis(problem: TSPProblem, path: Array[Int]): Int = (path :+ path(0)).sliding(2).map(a => problem.distance(a(0))(a(1))).sum
  def adjacentDis(problem: TSPProblem, path: Array[Int]): Int = path.map(i => { problem.distance(i)(path(i)) }).sum
}
