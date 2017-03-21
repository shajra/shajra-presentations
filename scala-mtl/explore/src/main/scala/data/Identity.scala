package scalaz
package data

case class Identity[A](run: A) extends AnyVal

object Test {
  def test
      [M[_]
      : MonadBase[Identity, ?[_]]
      : MonadReader[Int, ?[_]]
      : MonadState[String, ?[_]]
      ]
      : M[String] =
    for {
      i <- 1.pure[M]
      r <- MonadReader.ask
      _ <- (i + r).toString.put
      sum <- MonadState.get
    } yield sum

  def using =
    test[ReaderT[Int, StateT[String, Identity, ?], ?]].run(1).run("").run

}

object Identity extends IdentityTypes with IdentityInstances
