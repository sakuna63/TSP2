package common

import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by sakuna63 on 1/26/15.
 */
class CalcSpec extends FlatSpec with Matchers {

  "A result value" should "be equal to expected value" in {
    val dis = Array(
      Array(-1,10,20),
      Array(30,-1,40),
      Array(50,60,-1)
    )
    val path = Array(1,2,0)
    val expect = dis(0)(1) + dis(1)(2) + dis(2)(0)
    val result = Calc.adjacentDis(dis, path)

    result should be (expect)
  }
}
