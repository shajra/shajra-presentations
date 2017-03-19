package scalaz
package typeclass


import scala.language.implicitConversions
import scala.language.experimental.macros


trait MonadTransSyntax {

  implicit def monadTransOpsA[F[_] : Monad, A]
      (a: F[A]): MonadTransSyntax.OpsA[F, A] =
    new MonadTransSyntax.OpsA(a)

}


object MonadTransSyntax {

  class OpsA[F[_] : Monad, A](self: F[A]) {
    def liftT[G[_[_], _]](implicit G: MonadTrans[G]): G[F, A] = G.liftT(self)
  }



}
