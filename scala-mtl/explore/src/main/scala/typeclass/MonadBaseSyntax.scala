package scalaz
package typeclass


trait MonadBaseSyntax {

  implicit def monadErrorOps[B[_], A](fa: B[A]): MonadBaseSyntax.OpsA[B, A] =
    new MonadBaseSyntax.OpsA(fa)

}


object MonadBaseSyntax {

  class OpsA[B[_], A](self: B[A]) {
    def liftBase[M[_]](implicit M: MonadBase[B, M]): M[A] = M.liftBase(self)
  }

}
