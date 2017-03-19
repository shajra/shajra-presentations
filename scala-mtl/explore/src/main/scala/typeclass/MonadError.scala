package scalaz
package typeclass


trait MonadError[E, M[_]] {
  def monad: Monad[M]
  def raiseError[A](e: E): M[A]
  def handleError[A](fa: M[A])(f: E => M[A]): M[A]
}


object MonadError {
  def apply[E, M[_]](implicit M: MonadError[E, M]): MonadError[E, M] = M
}
