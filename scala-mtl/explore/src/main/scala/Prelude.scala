package scalaz

import typeclass._
import data._

import scala.language.implicitConversions

trait Prelude
    extends data.DisjunctionFunctions
    with data.DisjunctionSyntax
    with data.IdentityTypes
    with data.MaybeFunctions
    with data.MaybeSyntax
    with typeclass.FunctorFunctions
    with typeclass.FunctorSyntax
    with typeclass.ApplyFunctions
    with typeclass.ApplySyntax
    //with typeclass.ApplicativeFunctions
    with typeclass.ApplicativeSyntax
    with typeclass.BindFunctions
    with typeclass.BindSyntax
    //with typeclass.MonadFunctions
    with typeclass.MonadSyntax
    with typeclass.MonadBaseSyntax
    with typeclass.MonadErrorSyntax
    with typeclass.MonadStateSyntax
    //with typeclass.FoldableFunctions
    //with typeclass.MonadTransFunctions
    with typeclass.FoldableSyntax
    with typeclass.TraversableFunctions
    with typeclass.TraversableSyntax
    //with typeclass.ProfunctorFunctions
    with typeclass.ProfunctorSyntax
    //with typeclass.ChoiceFunctions
    with typeclass.ChoiceSyntax
    //with typeclass.StrongFunctions
    with typeclass.StrongSyntax {

  type Applicative[F[_]] = typeclass.Applicative[F]
  val Applicative = typeclass.Applicative

  type Apply[F[_]] = typeclass.Apply[F]
  val Apply = typeclass.Apply

  type Bind[M[_]] = typeclass.Bind[M]
  val Bind = typeclass.Bind

  type Foldable[T[_]] = typeclass.Foldable[T]
  val Foldable = typeclass.Foldable

  type Functor[F[_]] = typeclass.Functor[F]
  val Functor = typeclass.Functor

  type Monad[M[_]] = typeclass.Monad[M]
  val Monad = typeclass.Monad

  type Traversable[T[_]] = typeclass.Traversable[T]
  val Traversable = typeclass.Traversable

  type Profunctor[F[_,_]] = typeclass.Profunctor[F]
  val Profunctor = typeclass.Profunctor

  def Choice[P[_,_]](implicit P: Choice[P]): Choice[P] = P
  def Strong[P[_,_]](implicit P: Strong[P]): Strong[P] = P

  type MonadTrans[F[_[_], _]] = typeclass.MonadTrans[F]
  val MonadTrans = typeclass.MonadTrans

  type MonadBase[B[_], M[_]] = typeclass.MonadBase[B, M]
  val MonadBase = typeclass.MonadBase

  type MonadError[E, F[_]] = typeclass.MonadError[E, F]
  val MonadError = typeclass.MonadError

  type MonadState[S, F[_]] = typeclass.MonadState[S, F]
  val MonadState = typeclass.MonadState

  type MonadReader[R, F[_]] = typeclass.MonadReader[R, F]
  val MonadReader = typeclass.MonadReader

  type Semigroup[A] = typeclass.Semigroup[A]
  val Semigroup = typeclass.Semigroup

  type Monoid[A] = typeclass.Monoid[A]
  val Monoid = typeclass.Monoid

  type \/[L, R] = data.Disjunction.\/[L, R]
  val \/ = data.Disjunction

  type ===[A, B] = data.===[A, B]

  type Identity[A] = data.Identity[A]

  type Maybe[A] = data.Maybe[A]

  type EitherT[E, F[_], A] = data.EitherT[E, F, A]
  type ReaderT[R, F[_], A] = data.ReaderT[R, F, A]
  type StateT[S, F[_], A] = data.StateT[S, F, A]

}

object Prelude extends Prelude {
  object Base extends Prelude
  object Default extends Prelude with BaseHierarchy
}
