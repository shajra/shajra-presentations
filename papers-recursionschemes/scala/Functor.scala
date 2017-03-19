package shajra


trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def fmap[A, B](f: A => B)(fa: F[A]): F[B] = map(fa)(f)
}


object Functor {

  @inline def apply[F[_]](implicit ev: Functor[F]): Functor[F] = ev

  class Ops1[F[_], A](val fa: F[A]) extends AnyVal {
    def map[B](f: A => B)(implicit ev: Functor[F]): F[B] = ev.map(fa)(f)
  }

  class Ops2[A, B](val f: A => B) extends AnyVal {
    def fmap[F[_]](implicit ev: Functor[F]): F[A] => F[B] = ev fmap f
  }

  trait Syntax {
    implicit def toFunctorOps1[F[_] : Functor, A](fa: F[A]) = new Ops1(fa)
    implicit def toFunctorOps2[A, B](f: A => B) = new Ops2(f)
  }

  object Syntax extends Syntax

}
