package scalaz


import typeclass._


trait BaseHierarchy extends BaseHierarchy.BH9


object BaseHierarchy {

  object Default extends BaseHierarchy

  trait BH9 extends BH8 {
    implicit def monadBaseMMonad[B[_], M[_]](implicit M: MonadBase[B, M]): Monad[M] = M.monad
  }

  trait BH8 extends BH7 {
    implicit def monadBaseBMonad[B[_], M[_]](implicit M: MonadBase[B, M]): Monad[B] = M.monadBase
  }

  trait BH7 extends BH6 {
    implicit def monadErrorMonad[E, M[_]](implicit M: MonadError[E, M]): Monad[M] = M.monad
  }

  trait BH6 extends BH5 {
    implicit def monadReaderMonad[R, M[_]](implicit M: MonadReader[R, M]): Monad[M] = M.monad
  }

  trait BH5 extends BH4 {
    implicit def monadStateMonad[S, M[_]](implicit M: MonadState[S, M]): Monad[M] = M.monad
  }

  trait BH4 extends BH3 {
    implicit def choiceProfunctor[P[_, _]](implicit P: Choice[P]): Profunctor[P] = P.profunctor
    implicit def monadBind[M[_]](implicit M: Monad[M]): Bind[M] = M.bind
    implicit def monadApplicative[M[_]](implicit M: Monad[M]): Applicative[M] = M.applicative
    implicit def monadApply[M[_]](implicit M: Monad[M]): Apply[M] = M.applicative.apply
    implicit def monadFunctor[M[_]](implicit M: Monad[M]): Functor[M] = M.applicative.apply.functor
    implicit def monoidSemigroup[A](implicit A: Monoid[A]): Semigroup[A] = A.semigroup
    implicit def traversableFoldable[T[_]](implicit T: Traversable[T]): Foldable[T] = T.foldable
  }

  trait BH3 extends BH2 {
    implicit def bindApply[M[_]](implicit M: Bind[M]): Apply[M] = M.apply
    implicit def bindFunctor[M[_]](implicit M: Bind[M]): Functor[M] = M.apply.functor
    implicit def strongProfunctor[P[_, _]](implicit P: Strong[P]): Profunctor[P] = P.profunctor
  }

  trait BH2 extends BH1 {
    implicit def applicativeApply[M[_]](implicit M: Applicative[M]): Apply[M] = M.apply
    implicit def applicativeFunctor[M[_]](implicit M: Applicative[M]): Functor[M] = M.apply.functor
  }

  trait BH1 extends BH0 {
    implicit def applyFunctor[M[_]](implicit M: Apply[M]): Functor[M] = M.functor
  }

  trait BH0 {
    implicit def traversableFunctor[T[_]](implicit T: Traversable[T]): Functor[T] = T.functor
  }

}
