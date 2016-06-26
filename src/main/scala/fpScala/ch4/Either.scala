package fpScala.ch4

//hide std library `Option` and `Either`, since we are writing our own in this chapter
import scala.{Option => _, Some => _, Either => _, Left => _, Right => _, _}
/*
  the bigger idea is that we can represent exceptions as ordinary values and use higher-order functions to encapsulate 
  common patterns of handling and propagating errors. This general idea, of representing effects as values.
  
  exceptions should be reserved only for truly unrecoverable conditions
 */


/**
 * Either is an "algebraic data type" that has only two cases (just like Option):
 * 1. the Right _data constructor_ is used for the success case
 * 2. the Left _data constructor_ is used for the failure case 
 * 
 * The essential difference is that both cases carry a value. 
 * The Either data type represents, in a very general way, values that can be one of two things. 
 * 
 * We can say that it’s a disjoint union of two types.
 */
sealed trait Either[+E, +A] {
/**
 * Continuing the analogy with [[scala.Option]], a `RightProjection` declares
 * that `Right` should be analogous to `Some` in some code.
 */
 
  /**
  * map can be used to `transform` the result inside an Either
  * Now we get information about the actual exception that occurred, 
  * rather than just getting back None in the event of a failure.
  */
  def map[B](f: A => B): Either[E, B] = this match {
    case Right(a) => Right(f(a))
    case Left(e) => Left(e)
  }

  /**
  *   flatMap is similar to map, except that the function we provide to transform the result is an Either, 
  *   function `f` is applied if the Either is a Right (Since we are mapping over the `right side`, 
  *   function f's type is  `A => Either[EE, B]` which A is Right's type parameter.
  *   
  *   When mapping over the `right side`, we must promote the left type parameter to some supertype `EE >: E`, 
  *   to satisfy the `+E` variance annotation
  */
  def flatMap[EE >: E, B](f: A => Either[EE, B]): Either[EE, B] =
    this match {
      case Left(e) => Left(e)
      case Right(a) => f(a)
    }
    
  /**
  *   When mapping over the `right side`, we must promote the left type parameter to some supertype `EE >: E`, 
  *   to satisfy the `+E` variance annotation.
  *   
  *   The `b` value is a non-strict or lazy argument, as indicated by the `=> A` as the type of a.
  */
  def orElse[EE >: E,B >: A](b: => Either[EE, B]): Either[EE, B] =
    this match {
      case Left(_) => b
      case Right(a) => Right(a)
    }
    
  /**
  *   map2 uses a for comprehension over both Either types. 
  *   Again when mapping over the right side, we must promote the left type parameter to some supertype `EE >: E`, 
  *   to satisfy the `+E` variance annotation
  */
  def map2[EE >: E, B, C](b: Either[EE, B])(f: (A, B) => C): Either[EE, C] =
    for {
      aa <- this
      bb <- b 
    } yield f(aa,bb)

} 
case class Left[+E](value: E) extends Either[E, Nothing]  // the Left _data constructor_ is used for the failure case
case class Right[+A](value: A) extends Either[Nothing, A] // the Right _data constructor_ is used for the success case

// Either is also often used more generally to encode one of two possibilities in cases where it isn’t worth defining a fresh data type.
object Either {
  /**
  * The mean example, this time returning a String in case of failure   
  */
  def mean(xs: IndexedSeq[Double]): Either[String, Double] =
    if (xs.isEmpty)
      Left("mean of empty list!")
    else
      Right(xs.sum / xs.length)

  /**
  * To include more information about the error such as a stack trace showing the location of the error in the source code. 
  * Simply return the exception in the Left side of an Either: 
  */
  def safeDiv(x: Int, y: Int): Either[Exception, Int] =
    try Right(x / y)
    catch { case e: Exception => Left(e) }

  //Try, which factors out this common pattern of converting thrown exceptions to values.
  //Try takes a non-strict or lazy argument, as indicated by the `=> A` as the type of a.
  def Try[A](a: => A): Either[Exception, A] =
    try Right(a)
    catch { case e: Exception => Left(e) }

  /**
  * traverse preforms a sequence and a map at the same time. It transforms List[A] with function `f: A => Either[E, B]`. 
  * It returns the first error on the Left or whole List on the Right. 
  */
  def traverse[E, A, B](as: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    as match {
      case Nil => Right(Nil)
      case h::t => (f(h) map2 traverse(t)(f))(_ :: _)
    }

  /**
  * Implement sequence using traverse and the identity function as the function in traverse. 
  * It returns the whole list on the Right or the first Left
  */
  def sequence[E, A](es: List[Either[E, A]]): Either[E, List[A]] =
    traverse(es)(x => x) // Identity function

  
  def traverse_1[E,A,B](es: List[A])(f: A => Either[E, B]): Either[E, List[B]] =
    es.foldRight[Either[E,List[B]]](Right(Nil))((a, b) => f(a).map2(b)(_ :: _))
    
  /*
  There are a number of variations on `Option` and `Either`. If we want to accumulate multiple errors, a simple
  approach is a new data type that lets us keep a list of errors in the data constructor that represents failures:
  
  trait Partial[+A,+B]
  case class Errors[+A](get: Seq[A]) extends Partial[A,Nothing]
  case class Success[+B](get: B) extends Partial[Nothing,B]
  
  There is a type very similar to this called `Validation` in the Scalaz library. You can implement `map`, `map2`,
  `sequence`, and so on for this type in such a way that errors are accumulated when possible (`flatMap` is unable to
  accumulate errors--can you see why?). This idea can even be generalized further--we don't need to accumulate failing
  values into a list; we can accumulate values using any user-supplied binary function.
  
  It's also possible to use `Either[List[E],_]` directly to accumulate errors, using different implementations of
  helper functions like `map2` and `sequence`.
  */
}  

object EitherTest extends App {
  //TODO: Test the functions that are defined for Either
}