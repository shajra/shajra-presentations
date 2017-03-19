package shajra


import Functor.Syntax._


trait Mu[F[_]] {
  def run[X](phi: Algebra[F, X]): X
}


object Mu {

  def in[F[_] : Functor](fMuF: F[Mu[F]]): Mu[F] =
    new Mu[F] {
      def run[X](phi: Algebra[F, X]) = phi(fMuF map { _ run phi })
    }

  def out[F[_] : Functor](muF: Mu[F]): F[Mu[F]] =
    muF.run[F[Mu[F]]] { _ map { fMuF => in(fMuF) } }

  implicit val fixLike: FixLike[Mu] =
    new FixLike[Mu] {
      def in[F[_] : Functor](fMuF: F[Mu[F]]) = Mu in fMuF
      def out[F[_] : Functor](muF: Mu[F]) = Mu out muF
    }

}


trait Nu[F[_]] {
  type A
  val a: A
  val run: Coalgebra[F, A]
}


object Nu {

  def apply[F[_], B](b: B)(psi: Coalgebra[F, B]): Nu[F] =
    new Nu[F] {
      type A = B
      val a = b
      val run = psi
    }

  def in[F[_] : Functor](fNuF: F[Nu[F]]): Nu[F] =
    Nu(fNuF) { _ map { nuF => out(nuF) } }

  def out[F[_] : Functor](nuF: Nu[F]): F[Nu[F]] =
    nuF run nuF.a map { Nu(_)(nuF.run) }

  implicit val fixLike: FixLike[Nu] =
    new FixLike[Nu] {
      def in[F[_] : Functor](fNuF: F[Nu[F]]) = Nu in fNuF
      def out[F[_] : Functor](muF: Nu[F]) = Nu out muF
    }

}


trait Schemes {

  def fix[X, R](a: (X => R) => X => R): X => R = x => a(fix(a))(x)

  def banana[G[_[_]] : FixLike, F[_] : Functor, X]
      (phi: Algebra[F, X]): G[F] => X =
    //
    // NOTE: this definition is also valid; we use the one from the theory
    //
    //    muF => muF run phi
    //
    fix[G[F], X] { f => muF => phi(f.fmap[F] apply (FixLike[G].out(muF))) }

  def lens[G[_[_]] : FixLike, F[_] : Functor, X]
      (psi: Coalgebra[F, X]): X => G[F] =
    //
    // NOTE: this recurses infinitely but is the definition from the theory
    //
    //
    //x => Nu(x)(psi)
    fix[X, G[F]] { f => x => FixLike[G].in(f.fmap[F] apply (psi(x))) }

  def envelope[F[_] : Functor, X]
      (psi: Coalgebra[F, X])(phi: Algebra[F, X]): X => X =
    fix[X, X] { f => x => phi(f.fmap[F] apply (psi(x))) }

}


object Schemes extends Schemes
