package scalaz
package data


import Disjunction.{\/-, -\/}


trait DisjunctionFunctions {

  @inline def left[L, R](value: L): Disjunction[L, R] = -\/(value)

  @inline def right[L, R](value: R): Disjunction[L, R] = \/-(value)

  def swap[L, R](ab: L \/ R): R \/ L = ab.fold[R \/ L](\/-(_))(-\/(_))

  def fromEither[L, R](ab: Either[L, R]): L \/ R = ab.fold(-\/(_), \/-(_))

  def either[A, B, C](ac: A => C)(bc: B => C): A \/ B => C = _ match {
    case -\/(l)  => ac(l)
    case \/-(r) => bc(r)
  }

}
