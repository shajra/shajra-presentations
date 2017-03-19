package scalaz
package typeclass


trait MonadReaderFunctions {

  def ask[F[_], R](implicit F: MonadReader[R, F]): F[R] = F.ask

}
