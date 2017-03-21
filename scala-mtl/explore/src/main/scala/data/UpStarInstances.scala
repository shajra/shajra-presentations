package scalaz
package data


trait UpStarInstances {

  implicit def upstarProfunctor[F[_]](implicit F: Functor[F])
      : Profunctor[UpStar[F, ?, ?]] = new Profunctor[UpStar[F, ?, ?]] {

    def lmap[A, B, C](fab: UpStar[F, A, B])(ca: C => A): UpStar[F, C, B] =
      UpStar(c => fab.run(ca(c)))

    def rmap[A, B, C](fab: UpStar[F, A, B])(bc: B => C): UpStar[F, A, C] =
      UpStar(a => F.map(fab.run(a))(bc))

    def dimap[A, B, C, D](fab: UpStar[F, A, B])(ca: C => A)(bd: B => D)
        : UpStar[F, C, D] =
      UpStar(c => F.map(fab.run(ca(c)))(bd))

  }

  /*
  implicit val upStarMonad: Monad[UpStar] =
    new MonadClass.Template[UpStar] {
      def ap[A, B](ua: UpStar[A])(f: UpStar[A => B]): UpStar[B] =
        xs.flatMap(a => f.map(_(a)))
      def flatMap[A, B](xs: UpStar[A])(f: A => UpStar[B]): UpStar[B] =
        xs.flatMap(f)
      def map[A, B](xs: UpStar[A])(f: A => B): UpStar[B] = xs.map(f)
      def pure[A](a: A): UpStar[A] = UpStar(a)
    }
   */

  implicit def upStarMonadTrans[A]
      : MonadTrans[λ[(F[_], B) => UpStar[F, A, B]]] =
    new MonadTrans[λ[(F[_], B) => UpStar[F, A, B]]] {
      def liftT[F[_] : Monad, B](a: F[B]): UpStar[F, A, B] = UpStar(_ => a)
    }

}
