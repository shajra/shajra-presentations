package scalaz
package data


import scalaz.typeclass.
  { ApplyClass, ApplicativeClass, FunctorClass, MonadClass }


trait EitherTInstances extends EitherTInstances.Instances0


object EitherTInstances {

  trait Instances0 extends Instances1 {

    implicit def eitherTMonad[E, F[_]](implicit M0: Monad[F])
        : Monad[EitherT[E, F, ?]] =
      new EitherTMonad[E, F] { val M = M0 }

    implicit def eitherTMonadTrans[E]
        : MonadTrans[λ[(F[_], A) => EitherT[E, F, A]]] =
      new MonadTrans[λ[(F[_], A) => EitherT[E, F, A]]] {
        def liftT[F[_] : Monad, B](a: F[B]) = EitherT(a.map(\/.right))
      }

    implicit def eitherTMonadBase[R, B[_], F[_]](implicit M: MonadBase[B, F])
       : MonadBase[B, EitherT[R, F, ?]] =
      new MonadBase[B, EitherT[R, F, ?]] {
        def monad = eitherTMonad(M.monad)
        def monadBase = M.monadBase
        def liftBase[A](base: B[A]) =
          M.liftBase(base).liftT[EitherT[R, ?[_], ?]]
      }

    implicit def eitherTMonadError[E, F[_]]
        (implicit F: Monad[F]): MonadError[E, EitherT[E, F, ?]] =
      new MonadError[E, EitherT[E, F, ?]] {
        val monad = eitherTMonad(F)
        def raiseError[A](e: E) = EitherT.raiseError(e)
        def handleError[A](fa: EitherT[E, F, A])(f: E => EitherT[E, F, A]) =
          EitherT.handleError(fa)(f)
      }

    implicit def eitherTMonadReader[E, R, F[_]]
        (implicit MR: MonadReader[R, F]): MonadReader[R, EitherT[E, F, ?]] =
      new MonadReader[R, EitherT[E, F, ?]] {
        val monad = new EitherTMonad[E, F] { val M = MR.monad }
        def ask = MR.ask.liftT[EitherT[E, ?[_], ?]]
        def local[A](ma: EitherT[E, F, A])(f: R => R) =
          EitherT(MR.local(ma.run)(f))
      }

    implicit def eitherTMonadState[E, S, F[_]]
        (implicit MS: MonadState[S, F]): MonadState[S, EitherT[E, F, ?]] =
      new MonadState[S, EitherT[E, F, ?]] {
        val monad = new EitherTMonad[E, F] { val M = MS.monad }
        def get = MS.get.liftT[EitherT[E, ?[_], ?]]
        def put(s: S) = MS.put(s).liftT[EitherT[E, ?[_], ?]]
      }

  }

  trait Instances1 extends Instances2 {

    implicit def eitherTApplicative[E, F[_]](implicit A0: Applicative[F])
        : Applicative[EitherT[E, F, ?]] =
      new EitherTApplicative[E, F] { val AP = A0 }

  }

  trait Instances2 extends Instances3 {

    implicit def eitherTApply[E, F[_]](implicit A0: Apply[F])
        : Apply[EitherT[E, F, ?]] =
      new EitherTApply[E, F] { val A = A0 }

  }

  trait Instances3 {

    implicit def eitherTFunctor[E, F[_]](implicit F0: Functor[F])
        : Functor[EitherT[E, F, ?]] =
      new EitherTFunctor[E, F] { val F = F0 }

  }

  private abstract class EitherTMonad[E, F[_]]
      extends EitherTApplicative[E, F]
      with MonadClass.Template[EitherT[E, F, ?]] {
    implicit def M: Monad[F]
    def AP = M.applicative
    def flatMap[A, B](ma: EitherT[E, F, A])(f: A => EitherT[E, F, B]) =
      EitherT(ma.run.flatMap(_.fold(e => (\/.left[E, B](e)).pure)(b => f(b).run)))
  }

  private abstract class EitherTApplicative[E, F[_]]
      extends EitherTApply[E, F]
      with ApplicativeClass[EitherT[E, F, ?]] {
    implicit def AP: Applicative[F]
    def A = AP.apply
    def pure[A](a: A) = EitherT((\/ right a).pure[F])
  }

  private abstract class EitherTApply[E, F[_]]
      extends EitherTFunctor[E, F]
      with ApplyClass[EitherT[E, F, ?]] {
    implicit def A: Apply[F]
    def F = A.functor
    def ap[A, B](fa: EitherT[E, F, A])(f: EitherT[E, F, A => B]) =
      EitherT(Apply.apply2(f.run, fa.run)((a, b) => b ap a))
  }

  private abstract class EitherTFunctor[E, F[_]]
      extends FunctorClass[EitherT[E, F, ?]] {
    implicit def F: Functor[F]
    def map[A, B](fa: EitherT[E, F, A])(f: A => B) =
      EitherT(fa.run map (_ map f))
  }

}
