package scalaz
package typeclass


trait ApplyFunctions {

  def ap[F[_], A,B]
      (fa: => F[A])(f: => F[A => B])(implicit F: Apply[F]): F[B] =
    F.ap(fa)(f)

  def ap2[F[_], A, B, C]
      (fa: => F[A], fb: => F[B])
      (f: F[(A,B) => C])
      (implicit F: Apply[F])
      : F[C] =
    ap(fb)(ap(fa)(f.map(_.curried)))

  def apply2[F[_], A, B, C]
      (fa: => F[A], fb: => F[B])
      (f: (A, B) => C)
      (implicit F: Apply[F])
      : F[C] = {
    ap(fb)(fa.map(f.curried))
  }

}
