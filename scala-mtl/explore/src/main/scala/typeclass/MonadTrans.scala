package scalaz
package typeclass


trait MonadTrans[F[_[_], _]] {
  def liftT[G[_] : Monad, A](a: G[A]): F[G, A]
}


object MonadTrans {
  def apply[F[_[_], _]](implicit F: MonadTrans[F]): MonadTrans[F] = F
}
