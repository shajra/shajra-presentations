package scalaz
package data


sealed trait Disjunction[L, R] {
  import Disjunction.{-\/, \/-}
  final def fold[A](la: L => A)(ra: R => A): A = this match {
    case -\/(l) => la(l)
    case \/-(r) => ra(r)
  }
}


object Disjunction extends DisjunctionInstances with DisjunctionFunctions {

  object Syntax extends DisjunctionSyntax

  type \/[L, R] = Disjunction[L, R]
  val \/ = this

  case class -\/[L, R](value: L) extends (L \/ R)
  case class \/-[L, R](value: R) extends (L \/ R)

}
