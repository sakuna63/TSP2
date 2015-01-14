package solver

import scala.collection.immutable.IndexedSeq

/**
 * Created by sakuna63 on 1/14/15.
 */
class MMAntColonySolver(m:Int, alpha:Int, beta:Int, rho:Double, seed:Long)
  extends AntColonySolver(m:Int, alpha:Int, beta:Int, rho:Double, seed:Long) {

  override def refreshPheromone(pheromones: Array[Array[Double]],
                                pathAndDistances: IndexedSeq[(Array[Int], Double)]): Array[Array[Double]] = {
    val best = pathAndDistances.minBy(_._2)
    pheromones.zipWithIndex.map({ case(arr, i) =>
      arr.zipWithIndex.map({ case(p, l) =>
        // τ_ij_best
        val t: Double = if (best._1(i) == l || best._1(l) == i ) 1.0 / _._2 else 0.0
        (1 - rho) * p + t
      })
    })
  }
}
