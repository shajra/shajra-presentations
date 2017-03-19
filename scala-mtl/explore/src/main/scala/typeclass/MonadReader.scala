package scalaz
package typeclass


trait MonadReader[R, M[_]] {
  def monad: Monad[M]
  def ask: M[R]
}


object MonadReader extends MonadReaderFunctions {
  def apply[R, M[_]](implicit M: MonadReader[R, M]): MonadReader[R, M] = M
}
