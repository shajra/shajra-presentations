package scalaz
package data


import scalaz.typeclass.
  { ApplyClass, ApplicativeClass, BindClass, FunctorClass, MonadClass,
    MonadBaseClass }


trait ReaderTInstances extends ReaderTInstances.Instances0


object ReaderTInstances {

  abstract class Instances0 extends Instances1 {

    implicit def readerTMonad[R, F[_]](implicit M: Monad[F])
        : Monad[ReaderT[R, F, ?]] =
      new ReaderTMonad[R, F] { val B = M.bind; val AP = M.applicative }

    implicit def readerTMonadTrans[E]
        : MonadTrans[λ[(F[_], A) => ReaderT[E, F, A]]] =
      new MonadTrans[λ[(F[_], A) => ReaderT[E, F, A]]] {
        def liftT[F[_] : Monad, B](a: F[B]) = ReaderT(_ => a)
      }

    implicit def readerTMonadBase[R, B[_], F[_]](implicit M: MonadBase[B, F])
       : MonadBase[B, ReaderT[R, F, ?]] =
      new MonadBase[B, ReaderT[R, F, ?]] {
        def monad = readerTMonad(M.monad)
        def monadBase = M.monadBase
        def liftBase[A](base: B[A]) =
          M.liftBase(base).liftT[ReaderT[R, ?[_], ?]]
      }

    implicit def readerTMonadReader[R, F[_]]
        (implicit F: Monad[F]): MonadReader[R, ReaderT[R, F, ?]] =
      new MonadReader[R, ReaderT[R, F, ?]] {
        val monad = readerTMonad(F)
        def ask: ReaderT[R, F, R] = ReaderT.ask
      }

    implicit def readerTMonadState[R, S, F[_]]
        (implicit MS: MonadState[S, F]): MonadState[S, ReaderT[R, F, ?]] =
      new MonadState[S, ReaderT[R, F, ?]] {
        val monad =
          new ReaderTMonad[R, F] {
            val B = MS.monad.bind
            val AP = MS.monad.applicative
          }
        def get = MS.get.liftT[ReaderT[R, ?[_], ?]]
        def put(s: S) = MS.put(s).liftT[ReaderT[R, ?[_], ?]]
      }

    implicit def readerTMonadError[R, E, F[_]]
        (implicit ME: MonadError[E, F]): MonadError[E, ReaderT[R, F, ?]] =
      new MonadError[E, ReaderT[R, F, ?]] {
        val monad =
          new ReaderTMonad[R, F] {
            val B = ME.monad.bind
            val AP = ME.monad.applicative
          }
        def handleError[A](fa: ReaderT[R, F, A])(f: E => ReaderT[R, F, A]) =
          ReaderT { r => ME.handleError(fa run r) { e => f(e) run r } }
        def raiseError[A](e: E) =
          ME.raiseError(e).liftT[ReaderT[R, ?[_], ?]]
      }

  }

  abstract class Instances1 extends Instances2 {

    implicit def readerTBind[R, F[_]](implicit B0: Bind[F])
        : Bind[ReaderT[R, F, ?]] =
      new ReaderTBind[R, F] { val B = B0 }

  }

  abstract class Instances2 extends Instances3 {

    implicit def readerTApplicative[R, F[_]](implicit AP0: Applicative[F])
        : Applicative[ReaderT[R, F, ?]] =
      new ReaderTApplicative[R, F] { val AP = AP0 }

  }

  abstract class Instances3 extends Instances4 {

    implicit def readerTApply[R, F[_]](implicit A0: Apply[F])
        : Apply[ReaderT[R, F, ?]] =
      new ReaderTApply[R, F] { val A = A0 }

  }

  abstract class Instances4 {

    implicit def readerTFunctor[R, F[_]](implicit F0: Functor[F])
        : Functor[ReaderT[R, F, ?]] =
      new ReaderTFunctor[R, F] { val F = F0 }

  }

  private trait ReaderTMonad[R, F[_]]
      extends ReaderTBind[R, F]
      with ReaderTApplicative[R, F]
      with MonadClass.Template[ReaderT[R, F, ?]]

  private trait ReaderTBind[R, F[_]]
      extends ReaderTApply[R, F]
      with BindClass.Template[ReaderT[R, F, ?]]
      with BindClass.FlatMap[ReaderT[R, F, ?]] {
    implicit def B: Bind[F]
    def A = B.apply
    def flatMap[A, B](ma: ReaderT[R, F, A])(f: A => ReaderT[R, F, B]) =
      ReaderT(r => ma.run(r).flatMap(a => f(a).run(r)))
  }

  private trait ReaderTApplicative[R, F[_]]
      extends ReaderTApply[R, F]
      with ApplicativeClass[ReaderT[R, F, ?]] {
    implicit def AP: Applicative[F]
    override def A = AP.apply
    def pure[A](a: A) = ReaderT(_ => a.pure[F])
  }

  private trait ReaderTApply[R, F[_]]
      extends ReaderTFunctor[R, F]
      with ApplyClass[ReaderT[R, F, ?]] {
    implicit def A: Apply[F]
    def F = A.functor
    def ap[A, B](fa: ReaderT[R, F, A])(f: ReaderT[R, F, A => B]) =
      ReaderT(Apply.apply2(f.run, fa.run)((a, b) => b ap a))
  }

  private trait ReaderTFunctor[R, F[_]]
      extends FunctorClass[ReaderT[R, F, ?]] {
    implicit def F: Functor[F]
    def map[A, B](fa: ReaderT[R, F, A])(f: A => B) =
      ReaderT(fa.run map (_ map f))
  }

}
