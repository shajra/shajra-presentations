package shajra


trait Aliases {

  type Algebra[F[_], A] = F[A] => A

  type Coalgebra[F[_], A] = A => F[A]

}


object Aliases
