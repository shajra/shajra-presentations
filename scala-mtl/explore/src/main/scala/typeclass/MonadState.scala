package scalaz
package typeclass


trait MonadState[S, M[_]] {
  def monad: Monad[M]
  def get: M[S]
  def put(s: S): M[Unit]
}


object MonadState extends MonadStateFunctions {
  def apply[S, M[_]](implicit M: MonadState[S, M]): MonadState[S, M] = M
}
