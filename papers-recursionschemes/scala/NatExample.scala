package shajra


sealed abstract class NatF[X]
final case class Zero[X]() extends NatF[X]
final case class Succ[X](x: X) extends NatF[X]


object NatF {
  implicit val functor: Functor[NatF] =
    new Functor[NatF] {
      def map[A, B](na: NatF[A])(f: A => B) =
        na match {
          case Zero() => Zero()
          case Succ(x) => Succ(f(x))
        }
    }
}


final case class Nat(repr: Mu[NatF]) extends AnyVal {

  def toInt: Int =
    Schemes.banana[Mu, NatF, Int] {
      case Zero() => 0
      case Succ(x) => 1 + x
    } apply repr

  def succ = Nat succ this

}


object Nat {
  def zero: Nat = Nat(Mu in Zero())
  def succ(n: Nat) = Nat(Mu in Succ(n.repr))
}


object NatExample extends App {
  println(Nat.zero.succ.succ.succ.toInt)
}
