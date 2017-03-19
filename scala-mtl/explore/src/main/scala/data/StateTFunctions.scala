package scalaz
package data


trait StateTFunctions {

  def get[F[_] : Applicative, S]: StateT[S, F, S] =
    StateT { s => (s, s).pure[F] }

  def put[F[_] : Applicative, S](s: S): StateT[S, F, Unit] =
    StateT { _ => (s, ()).pure[F] }

}
