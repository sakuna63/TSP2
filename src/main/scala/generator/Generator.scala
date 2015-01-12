package generator

import common.TSPProblem

/**
 * Created by sakuna63 on 12/25/14.
 */
trait Generator {
//  def generate(problem: TSPProblem, start: Int, representationType: RepresentationType): Array[Int] = {
//    representationType match {
//      case RepresentationType.PATH => path(problem, start)
//      case RepresentationType.ADJACENT => adjacent(problem, start)
//      case RepresentationType.ORDER => order(problem, start)
//    }
//  }

  def path(problem: TSPProblem, start: Int): Array[Int]
  def adjacent(problem: TSPProblem, start: Int): Array[Int]
  def order(problem: TSPProblem, start: Int): Array[Int]

//  object RepresentationType {
//    case object PATH extends RepresentationType
//    case object ADJACENT extends RepresentationType
//    case object ORDER extends RepresentationType
//  }
//
//  class RepresentationType {}
}
