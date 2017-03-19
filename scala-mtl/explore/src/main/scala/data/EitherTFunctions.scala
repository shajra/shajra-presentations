package scalaz
package data


trait EitherTFunctions {

  def raiseError[E, F[_] : Applicative, A](e: E): EitherT[E, F, A] =
    EitherT(e.left[A].pure[F])

  def handleError[E, F[_] : Bind, A]
      (fa: EitherT[E, F, A])(f: E => EitherT[E, F, A]) =
    EitherT(fa.run.flatMap { _.fold(f(_).run)(_ => fa.run) })

}
