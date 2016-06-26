package fpScala.ch5

/*****
Warm up Notes:
  a `strict` function always evaluates all its arguments
  a `non-strict` or lazy function can choose to pass its arguments unevaluated
  Laziness can be used to improve the efficiency and modularity of functional programs using `lazy lists`, or `streams` 
  
  @strictness a formal definition
    If the evaluation of an expression runs forever or throws an error instead of returning
    a definite value, we say that the expression doesn’t terminate, or that it evaluates to
    bottom. A function `f` is "strict" if the expression `f(x)` evaluates to bottom for all `x` that
    evaluate to bottom.
  
 @Thunks
    A value of type `() => A` is a function that accepts zero arguments and returns an A. 
    In general, the unevaluated form of an expression is called a `thunk`, 
    and we can `force` the `thunk` to evaluate the expression and get a result.
 @Forcing a `thunk` by invoking the function, passing an empty argument list `()` 
  
  A nicer syntax for "non-strict" by-name arguments scala provides: 
  <code>
    def ifX[A](cond: Boolean, onTrue: => A, onFalse: => A): A =
      if (cond) onTrue else onFalse
  </code>  
  
  The arguments we’d like to pass unevaluated have an arrow => immediately before their type. 
  In the body of the function, we don’t need to do anything special to evaluate an argument annotated with =>. 
  We just reference the identifier as usual. Nor do we have to do anything special to call this function. 
  We just use the normal function call syntax, and Scala takes care of wrapping the expression in a thunk for us
  
  @Key_points:
    * The ability the Stream has to only evaluate the portion actually demanded is very useful.
    * Memoizing streams and avoiding recomputation  by caching values and using smart constructors
    * Chains of transformations on streams are fused into a single pass through the use of laziness.   
*****/

/**
 * This `algebraic data type` looks identical to List type, except that the Cons _data constructor_ takes 
 * explicit `thunks` ( () => A and () => Stream[A] ) aka "non-strict" values, instead of regular "strict" values. 
 */
sealed trait Stream[+A] {
import Stream._
  /**
  * headOption must explicitly force the h `thunk` via h().
  * In this example, we don’t evaluate the tail of the Cons
  */
  def headOption: Option[A] = this match {
    case Empty => None
    case Cons(h, t) => Some(h()) 
  }

  /**
  * Helper functions for inspecting streams 
  * The natural recursive solution will `stack overflow` on large streams, since its not tail recursive.
  */
  def toListRecursive: List[A] = this match {
    case Empty => Nil
    case Cons(h,t) => h() :: t().toListRecursive
  }

  /**
  * The tail-recursive Helper function for inspecting streams 
  * At each step we cons onto the front of the `list` list, which will result in the reverse of the stream. 
  * Then at the end we reverse the result to get the correct order again.
  */
  def toList: List[A] = {
    @annotation.tailrec
    def init(stream: Stream[A], list: List[A]):List[A] = stream match {
      case Cons(h,t) => init(t(), h() :: list)
      case _ => list
    }
    init(this, List()).reverse
  }

  /**
  In order to avoid the `reverse` at the end, we could write it using a
  mutable list buffer and an explicit recursive loop instead. Note that the mutable
  list buffer never escapes our `toList` method, so this function is
  still _pure_.
  */
  def toListFast: List[A] = {
    val buf = new collection.mutable.ListBuffer[A]
    @annotation.tailrec
    def go(s: Stream[A]): List[A] = s match {
      case Cons(h,t) =>
        buf += h()
        go(t())
      case _ => buf.toList
    }
    go(this)
  }

  /**
  * Create a new Stream[A] from taking the `n` first elements from this. 
  * Cases:
  *   If n > 1, recursively calling take on the invoked tail of a cons cell. 
  *   If n == 1, tail is not invoked
  *   If n == 0, we can avoid looking at the stream at all.
  */
  def take(n: Int): Stream[A] = this match {
    case Cons(h, t) if n > 1 => cons(h(), t().take(n - 1))
    case Cons(h, _) if n == 1 => cons(h(), empty)
    case _ => empty
  }

  /**
  * Create a new Stream[A] from this, but ignore the `n` first elements. This can be achieved by recursively calling
  * drop on the invoked tail of a cons cell. Note that the implementation is also tail recursive.
  */
  final def drop(n: Int): Stream[A] = this match {
    case Cons(h,t) if n > 0 => t().drop(n - 1)
    case _ => this
  }

  /**
  * similar to take, but returning all starting elements of the stream that match the predicate `p`
  */
  def takeWhile(p: A => Boolean):Stream[A] = this match {
    case Cons(h,t) if p(h()) => cons(h(), t().takeWhile(p))
    case _ => empty
  }
  
  /*
    A major theme of functional programming is "separation on concerns". 
    separate descriptions of computations from actually running them.
    Examples: 
      First-class functions capture some computation in their bodies but only execute it once they receive their arguments.
      Option captures the fact that an error occurred, where the decision of what to do about it became a separate concern.
    
    Laziness lets us seperate the description of an expression from the evaluation of that expression.  
   */
  
  /**
  * If p(h()) returns true, then exists terminates the traversal early and returns true as well. 
  * Remember the tail of the stream is a lazy val. 
  * So not only does the traversal terminate early, the tail of the stream is never evaluated at all! 
  */
  def existsExplicit(p: A => Boolean):Boolean = this match {
    case Cons(h,t) => p(h()) || t().existsExplicit(p) // Note that || is non-strict in its second argument.
    case _ => false
  }

  /**
  * 
  * the combining function `f` takes its second argument `=> B` by-name
  * If `f` chooses not to evaluate its second parameter, this terminates the traversal early. 
  */
  def foldRight[B](z: => B)(f: (A, => B) => B): B =
    this match {
      case Cons(h,t) => f(h(), t().foldRight(z)(f))
      case _ => z
    }

  /**
  * argument `b` is the unevaluated recursive step that folds the tail of the stream. 
  * If `p(a)` returns true, `b` will never be evaluated and the computation terminates early.
  * 
  * Since foldRight can terminate the traversal early, we can reuse it to implement exists. 
  * We can’t do that with a strict version of foldRight (meaning with List[A])
  */
  def exists(p: A => Boolean): Boolean = 
    foldRight(true)((h,t) => p(h) && t)
  
  //Laziness makes our code more reusable.

  /**
  * checks that all elements in the Stream match a given predicate. 
  * This will terminate the traversal if encounters a nonmatching value
  * 
  * Since `&&` is non-strict in its second argument, this terminates the traversal as soon as a nonmatching element is found.
  */
  def forAll(p: A => Boolean): Boolean = 
    foldRight(true)((h,t) => p(h) || t)
  
  def takeWhileViaFoldRight(p: A => Boolean): Stream[A] = 
    foldRight(empty[A])((h,t) => if(p(h)) cons(h,t) else empty )
  
  def headOptionViaFoldRight: Option[A] = 
    foldRight(None: Option[A])((h,_) => Some(h))
    
  
  //TODO: Continue on pg. 72
}

/**
  _data constructor_ that represents a `nonempty` stream, consists of a head and a tail, which are both "non-strict". 
  Due to technical limitations, these are `thunks` that must be A smart explicitly forced, rather than by-name parameters.
  
  If we use the Cons _data constructor_ directly the code will compute twice
 */
case class Cons[+A](h: () => A, t: () => Stream[A]) extends Stream[A] 
/** _data constructor_ that represents an `empty` stream */
case object Empty extends Stream[Nothing]

//companion object for Stream that contains two smart constructors and a varaidic (variable length) apply method
object Stream{
  /**
  * A smart constructor for creating a nonempty stream.
  * We cache the head and tail as lazy values to avoid repeated evaluation
  * 
  *   Scala takes care of wrapping the arguments to cons in thunks
  *   
  * cons smart constructor takes care of memoizing the by-name arguments for the head and tail of the Cons. 
  * This is a common trick, and it ensures that our thunk will only do its work once, when forced for the first time. 
  * Subsequent forces will return the cached lazy val
  * 
  * @hd the by-name argument (unevaluated), cached to lazy val `head` (memoizing) of the stream
  * @tl the by-name argument (unevaluated), cached to lazy val `tail` (memoizing) of the stream 
  */
  def cons[A](hd: => A, tl: => Stream[A]): Stream[A] = {
    lazy val head = hd
    lazy val tail = tl
    Cons(() => head, () => tail)
  }

  /**
  * A smart constructor for creating an empty stream of a particular type.
  * Returns Empty, as well as `annotates` Empty as a `Stream[A]`, which is better for type inference in some cases.
  * 
  * @Note Scala uses subtyping to represent data constructors, but we almost always want to infer Stream as 
  * the type, not Cons or Empty. Making smart constructors that return the base type is a common trick
  */
  def empty[A]: Stream[A] = Empty

  /**
  * The Variadic apply method
  * A convenient variable-argument method for constructing a Stream from multiple elements.
  * 
  * @Note Scala takes care of wrapping the arguments to cons in `thunks`, 
  * so the `as.head` and `apply(as.tail: _*)` expressions won’t be evaluated until we `force` the Stream. 
  */
  def apply[A](as: A*): Stream[A] =
    if (as.isEmpty) empty else cons(as.head, apply(as.tail: _*))
}

object StreamTest extends App {
  def double(x: Int) = x * 2
  val st = Stream(double(10),double(10),double(10))
  println("init: " + st)
  println("toList: " + st.toList)
  
}