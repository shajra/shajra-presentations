package shajra


trait FixLike[G[_[_]]] {
  def in[F[_] : Functor](fgf: F[G[F]]): G[F]
  def out[F[_] : Functor](gf: G[F]): F[G[F]]
}


object FixLike {
  def apply[G[_[_]]](implicit ev: FixLike[G]): FixLike[G] = ev
}
