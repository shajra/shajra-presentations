package scalaz
package data


trait ReaderTFunctions {

  def ask[F[_] : Applicative, R]: ReaderT[R, F, R] = ReaderT { _.pure[F] }

}
