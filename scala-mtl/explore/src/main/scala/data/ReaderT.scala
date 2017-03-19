package scalaz
package data


final case class ReaderT[A, F[_], B](run: A => F[B])


object ReaderT extends ReaderTInstances with ReaderTFunctions
