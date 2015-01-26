package common

/**
 * Created by sakuna63 on 1/7/15.
 */
object Calc {
  def adjacentDis(dis: Array[Array[Int]], path: Array[Int]): Int = path.map(i => { dis(i)(path(i)) }).sum
}
