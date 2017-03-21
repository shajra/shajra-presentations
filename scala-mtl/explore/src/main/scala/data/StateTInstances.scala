package scalaz
package data


import scalaz.typeclass.
  { ApplyClass, ApplicativeClass, BindClass, FunctorClass, MonadClass }


trait StateTInstances extends StateTInstances.Instances0


object StateTInstances {

  abstract class Instances0 extends Instances1 {

    implicit def stateTMonad[S, F[_]](implicit M0: Monad[F])
        : Monad[StateT[S, F, ?]] =
      new StateTMonad[S, F] { val M = M0 }

    implicit def stateTMonadTrans[S]
        : MonadTrans[λ[(F[_], A) => StateT[S, F, A]]] =
      new MonadTrans[λ[(F[_], A) => StateT[S, F, A]]] {
        def liftT[F[_] : Monad, B](a: F[B]) = StateT(s => a map { (s, _) })
      }

    implicit def stateTMonadBase[R, B[_], F[_]](implicit M: MonadBase[B, F])
       : MonadBase[B, StateT[R, F, ?]] =
      new MonadBase[B, StateT[R, F, ?]] {
        def monad = stateTMonad(M.monad)
        def monadBase = M.monadBase
        def liftBase[A](base: B[A]) =
          M.liftBase(base).liftT[StateT[R, ?[_], ?]]
      }

    implicit def stateTMonadState[S, F[_]]
        (implicit F: Monad[F]): MonadState[S, StateT[S, F, ?]] =
      new MonadState[S, StateT[S, F, ?]] {
        val monad = stateTMonad(F)
        def get = StateT.get
        def put(s: S) = StateT.put(s)
      }

   implicit def stateTMonadReader[S, R, F[_]]
        (implicit MR: MonadReader[R, F]): MonadReader[R, StateT[S, F, ?]] =
      new MonadReader[R, StateT[S, F, ?]] {
        val monad = new StateTMonad[S, F] { val M = MR.monad }
        def ask = MR.ask.liftT[StateT[S, ?[_], ?]]
        def local[A](ma: StateT[S, F, A])(f: R => R) =
          StateT(s => MR.local(ma.run(s))(f))
      }

    implicit def stateTMonadError[S, E, F[_]]
        (implicit ME: MonadError[E, F]): MonadError[E, StateT[S, F, ?]] =
      new MonadError[E, StateT[S, F, ?]] {
        val monad = new StateTMonad[S, F] { val M = ME.monad }
        def handleError[A](fa: StateT[S, F, A])(f: E => StateT[S, F, A]) =
          StateT { s => ME.handleError(fa run s) { e => f(e) run s } }
        def raiseError[A](e: E) =
          ME.raiseError(e).liftT[StateT[S, ?[_], ?]]
      }

  }

  abstract class Instances1 extends Instances2 {

    implicit def stateTBind[S, F[_]](implicit B0: Bind[F])
        : Bind[StateT[S, F, ?]] =
      new StateTBind[S, F] { val B = B0 }

  }

  abstract class Instances2 {

    implicit def stateTFunctor[S, F[_]](implicit F0: Functor[F])
        : Functor[StateT[S, F, ?]] =
      new StateTFunctor[S, F] { val F = F0 }

  }

  private trait StateTMonad[S, F[_]]
      extends StateTBind[S, F]
      with MonadClass.Template[StateT[S, F, ?]] {
    implicit def M: Monad[F]
    def B = M.bind
    def pure[A](a: A): StateT[S, F, A] = StateT(s => (s, a).pure)
  }

  private trait StateTBind[S, F[_]]
      extends StateTFunctor[S, F]
      with BindClass.Template[StateT[S, F, ?]]
      with BindClass.FlatMap[StateT[S, F, ?]] {
    implicit def B: Bind[F]
    def F = B.apply.functor
    def flatMap[A, B](fa: StateT[S, F, A])(f: A => StateT[S, F, B]) =
      StateT { fa run _ flatMap { case (s, a) => f(a).run(s) } }
  }

  private trait StateTFunctor[S, F[_]]
      extends FunctorClass[StateT[S, F, ?]] {
    implicit def F: Functor[F]
    def map[A, B](fa: StateT[S, F, A])(f: A => B) =
      StateT { fa run _ map { _ map f } }
  }

}
