package scalaz
package typeclass


import scala.language.implicitConversions
import scala.language.experimental.macros


trait MonadStateSyntax {

  implicit def monadStateOpsA[A](a: A): MonadStateSyntax.OpsA[A] =
    new MonadStateSyntax.OpsA(a)

}


object MonadStateSyntax {

  class OpsA[S](s: S) {
    def put[F[_]](implicit F: MonadState[S, F]): F[Unit] = F.put(s)
  }

}
