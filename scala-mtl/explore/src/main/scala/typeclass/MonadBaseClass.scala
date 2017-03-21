package scalaz
package typeclass


trait MonadBaseClass[B[_], M[_]] extends MonadBase[B, M] with MonadClass[M]


object MonadBaseClass {

  trait Endo[M[_]] extends MonadBaseClass[M, M] { self: MonadBase[M, M] =>
    def liftBase[A](base: M[A]) = base
    def monadBase = monad
  }

}
