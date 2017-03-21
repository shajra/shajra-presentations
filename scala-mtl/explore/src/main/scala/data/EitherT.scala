package scalaz
package data


final case class EitherT[E, F[_], A](run: F[E \/ A])


object EitherT extends EitherTInstances with EitherTFunctions
