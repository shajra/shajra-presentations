package scalaz
package typeclass


trait MonadBase[B[_], M[_]] {
  def monadBase: Monad[B]
  def monad: Monad[M]
  def liftBase[A](base: B[A]): M[A]
}


object MonadBase {
  def apply[B[_], M[_]](implicit M: MonadBase[B, M]): MonadBase[B, M] = M
}
