package scalaz
package typeclass


import scala.language.implicitConversions
import scala.language.experimental.macros


trait MonadSyntax {
  implicit def monadOps[M[_], A]
      (ma: M[A])(implicit M: Monad[M]): MonadSyntax.Ops[M, A] =
    new MonadSyntax.Ops(ma)
}


object MonadSyntax {
  class Ops[M[_], A](ma: M[A])(implicit M: Monad[M]) {
    def liftT[G[_[_], _]](implicit G: MonadTrans[G]): G[M, A] = G.liftT(ma)
  }
}
