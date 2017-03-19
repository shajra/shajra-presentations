package scalaz
package typeclass


import scala.language.implicitConversions
import scala.language.experimental.macros




trait MonadErrorSyntax {

  implicit def monadErrorOps[E, F[_], A]
      (fa: F[A])(implicit F: MonadError[E, F]): MonadErrorSyntax.Ops[E, F, A] =
    new MonadErrorSyntax.Ops(fa)

  implicit def monadErrorOpsA[A](a: A): MonadErrorSyntax.OpsA[A] =
    new MonadErrorSyntax.OpsA(a)

}


object MonadErrorSyntax {

  class Ops[E, F[_], A](self: F[A])(implicit F: MonadError[E, F]) {
    def handleError(f: E => F[A]): F[A] = F.handleError[A](self)(f)
  }

  class OpsA[E](e: E) {
    def raiseError[F[_], A](implicit F: MonadError[E, F]): F[A] =
      F.raiseError(e)
  }

}
