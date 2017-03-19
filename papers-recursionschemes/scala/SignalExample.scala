package shajra


final case class SignalF[A, X](head: A, tail: X)


final case class Signal[A](repr: Nu[SignalF[A, ?]]) extends AnyVal {

  import Signal.functor

  def toStream: Stream[A] = {
    val out = Nu.out[SignalF[A, ?]](repr)
    out.head #:: Signal(out.tail).toStream
  }

}


object Signal {

  def apply[A](a: A)(step: A => A): Signal[A] = {
    Signal(
      Schemes
        .lens[Nu, SignalF[A, ?], A] { _a => SignalF(_a, step(_a)) }
        .apply(a)
      //Nu.in[SignalF[A, ?]](a) { _a => SignalF(_a, step(_a)) }
    )
  }

  implicit def functor[X]: Functor[SignalF[X, ?]] =
    new Functor[SignalF[X, ?]] {
      def map[A, B](s: SignalF[X, A])(f: A => B) =
        SignalF(s.head, f(s.tail))
    }

}


object SignalExample extends App {

  val signal: Signal[Int] = Signal(0) { _ + 1 }
  println(signal.toStream.take(5).toList)

}
