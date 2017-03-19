package scalaz
package data


import scalaz.typeclass.Functor

import Disjunction.\/


final case class EitherT[E, F[_], A](run: F[E \/ A])


object EitherT extends EitherTInstances with EitherTFunctions
