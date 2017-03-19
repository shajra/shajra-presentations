package me.shajra.learn
package parsers


import scala.util.matching.Regex
import scalaz._
import scalaz.syntax.monadPlus._


case class Parser[A](parse: String => Option[(A, String)]) extends AnyVal {

  def map[B](f: A => B): Parser[B] =
    Parser { s => parse(s) map { case (a, rest) => (f(a), rest) } }

  def flatMap[B](f: A => Parser[B]): Parser[B] =
    Parser { s => parse(s) flatMap { case (a, rest) => f(a) parse rest } }

  def |(alt: => Parser[A]): Parser[A] =
    Parser { s => parse(s) orElse (alt parse s) }
    //Parser { s =>
    //  parse(s) match {
    //    case res@Some(a) => res
    //    case None => alt parse s
    //  }
    //}

  def ap[B](pf: => Parser[A => B]): Parser[B] =
    // flatMap { a => pf.map { f => f(a) } }
    for { a <- this; f <- pf } yield f(a)

  def + = some
  def some : Parser[List[A]] =
    for { a <- this; as <- many } yield (a :: as)

  def * = many
  def many : Parser[List[A]] = some | Parser.pass(Nil)

}


object Parser {

  def fail[A]: Parser[A] = Parser { s => None }

  // pure or unit or return or point
  def pass[A](a: => A): Parser[A] = Parser { s => Some((a, s)) }

  def alpha: Parser[String] = regex("[a-zA-Z]".r)

  def digit: Parser[String] = regex("[0-9]".r)

  implicit val monadPlus: MonadPlus[Parser] =
    new MonadPlus[Parser] {
      def point[A](a: => A) = Parser pass a
      def bind[A, B](p: Parser[A])(f: A => Parser[B]) = p flatMap f
      def plus[A](a1: Parser[A], a2: => Parser[A]) = a1 | a2
      def empty[A] = Parser.fail
    }

  private def regex(r: Regex): Parser[String] = Parser { s =>
    r.findPrefixOf(s) match {
      case Some(a) => Some((a, r.replaceFirstIn(s, "")))
      case None => None
    }
  }

}


object UseParser extends App {

  import Parser._

  val parser1 = for { a <- alpha; b <- digit } yield (a, b)
  assert(parser1.parse("1") == None)
  assert(parser1.parse("a1b") == Some((("a", "1"), "b")))

  val parser2 = alpha | digit
  assert(parser2.parse("a") == Some(("a", "")))
  assert(parser2.parse("1b") == Some(("1", "b")))
  assert(parser2.parse(".") == None)

  val parser3 = alpha.+
  assert(parser3.parse("aa1b") == Some((List("a", "a"), "1b")))

  val parser4 = alpha.*
  assert(parser4.parse("1b") == Some((Nil, "1b")))

  val parser5 = alpha <+> digit
  assert(parser5.parse("a") == Some(("a", "")))
  assert(parser5.parse("1b") == Some(("1", "b")))
  assert(parser5.parse(".") == None)

}
