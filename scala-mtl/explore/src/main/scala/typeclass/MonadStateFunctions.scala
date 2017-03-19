package scalaz
package typeclass


trait MonadStateFunctions {

  def get[S, F[_]](implicit F: MonadState[S, F]): F[S] = F.get

  def put[S, F[_]](s: S)(implicit F: MonadState[S, F]): Unit = F.put(s)

  def modify[S, F[_]](f: S => S)(implicit F: MonadState[S, F]): F[Unit] =
    F.get.flatMap { F put f(_) }


}
