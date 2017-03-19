package shajra


sealed abstract class StackF[A, X]
final case class Empty[A, X]() extends StackF[A, X]
final case class Push[A, X](a: A, x: X) extends StackF[A, X]


object StackF {
  implicit def functor[X]: Functor[StackF[X, ?]] =
    new Functor[StackF[X, ?]] {
      def map[A, B](s: StackF[X, A])(f: A => B) =
        s match {
          case Empty() => Empty()
          case Push(x, a) => Push(x, f(a))
        }
    }
}


final case class Stack[A](repr: Mu[StackF[A, ?]]) extends AnyVal {

  def toList: List[A] =
    repr.run[List[A]] {
      case Empty() => Nil
      case Push(a, x) => a :: x
    }

  def ::(a: A): Stack[A] = Stack.push(a, this)

}


object Stack {
  def empty[A]: Stack[A] = Stack(Mu.in[StackF[A, ?]](Empty()))
  def push[A](h: A, t: Stack[A]) = Stack(Mu.in[StackF[A, ?]](Push(h, t.repr)))
}


object StackExample extends App {

  val stack: Stack[Int] = 1 :: 2 :: 3 :: Stack.empty
  println(stack.toList)

  def factorial: Int => Int =
    Schemes.envelope[StackF[Int, ?], Int] { i =>
      if ( i > 0) Push(i, i - 1) else Empty()
    } {
      case Push(x, y) => x * y
      case Empty() => 1
    }

  println(factorial(5))

}
