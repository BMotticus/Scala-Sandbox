package sandbox.patterns.ch10.functional.monoids

/** 
  * A Monoid is a `purely algebraic structure`, which means that it is defined only by its `algebra`. 
  * All Monoids must conform to the so called `Monoid laws`.
  * 
  * The Monoid laws:
  *   A Monoid must have a TYPE - T
  *   A Monoid must have one ASSOCIATIVE BINARY OPERATION - `mappend`
  *   A structure must have an IDENTITY ELEMENT — `mzero`
  * 
  * Monoid is a type along with an `ASSOCIATIVE BINARY OPERATION` over it, which also has an `IDENTITY ELEMENT`. */
trait Monoid[T] {
  
  /** Monoid's `ASSOCIATIVE BINARY OPERATION`:
    *   This means that for any x, y, and z of the T type, the following is true: 
    *     {{{ op(op(x, y), z) == op(x, op(y, z)) }}}
    */
  def mappend(l: T, r: T): T

  /** Monoid's `IDENTITY ELEMENT`:
    *   This element is characterized by the fact that the previous operation will always return the other element: 
    *     {{{ op(x, zero) == x and op(zero, x) == x` }}}
    */
  def mzero: T
}

/** 
  * Companion object for Monoid
  * 
  * The `Monoid laws` are extremely simple but they give us great power to write polymorphic functions 
  * based just on the fact that monoids always conform to the same rules.
  * 
  * Package up the each implementation of monoid in an object called Monoid. 
  * 
  * The reason for that is Scala’s implicit resolution rules: 
  * When it needs an implicit parameter of some type, it’ll look for anything in scope. 
  * It’ll include the companion object of the type that you’re looking for.
  */
object Monoid {

  /**
    * Let's look at INTEGER ADDITION Monoid:
    *   TYPE: Int
    *   ASSOCIATIVE BINARY OPERATION: add. 
    *     It is indeed ASSOCIATIVE because ((1 + 2) + 3) == (1 + (2 + 3)).
    *   IDENTITY ELEMENT: 0. It does nothing when added to another integer.
    */
  implicit object IntAdditionMonoid extends Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  //or define an instance value with a return type
//  val intAddition: Monoid[Int] = new Monoid[Int] {
//    override def mappend(l: Int, r: Int): Int = l + r
//    val mzero: Int = 0
//  }

  /**
    * Let's look at INTEGER MULTIPLICATION:
    *   TYPE: Int
    *   ASSOCIATIVE BINARY OPERATION: multiply. 
    *     It is indeed ASSOCIATIVE because ((1 * 2) * 3) == (1 * (2 * 3)).
    *   IDENTITY ELEMENT: 1. any integer does nothing when multiplied by 1.
    */
  /*no implicit for Int to avoid ambiguous implicit values error with IntAdditionMonoid */ 
  object IntMultiplicationMonoid extends Monoid[Int] {
    override def mappend(l: Int, r: Int): Int = l * r
    val mzero: Int = 1
  }

  //or define an instance value with a return type
//  val intMultiplication: Monoid[Int] = new Monoid[Int] {
//    override def mappend(l: Int, r: Int): Int = l * r
//    val mzero: Int = 1
//  }

  /**
    * Let's look at STRING CONCATENATION:
    *   TYPE: String
    *   ASSOCIATIVE BINARY OPERATION: concatenate. 
    *     It is ASSOCIATIVE because (("bra" + "nd") + "on") == ("bra" + ("nd" + "on")).
    *   IDENTITY ELEMENT: an empty string. an empty string doesn't change a string.
    */
  implicit object StringConcatenationMonoid extends Monoid[String] {
    override def mappend(l: String, r: String): String = l + r
    val mzero: String = ""
  }

  //or define an instance value with a return type
//  val stringConcatenation: Monoid[String] = new Monoid[String] {
//    override def mappend(l: String, r: String): String = l + r
//    val mzero: String = ""
//  }
}

trait FoldLeft[F[_]] {
  def foldLeft[A, B](xs: F[A], b: B, f: (B, A) => B): B
}

object FoldLeft {

  implicit object FoldLeftList extends FoldLeft[List] {
    def foldLeft[A, B](xs: List[A], b: B, f: (B, A) => B): B = xs.foldLeft(b)(f)
  }
}

trait Identity[A] {
  val value: A
  
  def plus(a2: A)(implicit m: Monoid[A]): A = m.mappend(value, a2) 
}

trait MA[M[_], A] {
  val value: M[A]
  
  def summ(implicit m: Monoid[A], fl: FoldLeft[M]):A = fl.foldLeft(value, m.mzero, m.mappend)
}
// enrich my library. inject |+| to both Int and String with just one definition.
trait MonoidOp[A] {
  val F: Monoid[A]
  val value: A
  //the method being injected
  def |+|(a2: A): A = F.mappend(value, a2)
}

/**
  * Using the same technique, Scalaz also provides method injections for standard library types like Option and Boolean
  */
object syntax {
  //using Identity trait
  implicit def toIdent[A](a: A): Identity[A] = new Identity[A] {
    val value = a
  }
  // Enrich all types that has an instance for Monoid 
  implicit def toMonoidOp[A: Monoid](a: A): MonoidOp[A] = new MonoidOp[A] {
    val F = implicitly[Monoid[A]]
    val value = a
  }
  //usinf MA to sum a higher kinded type
  implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
    val value: M[A] = ma
  }
}

object Main {
  
  
/**
  * To use Monoid and FoldLeft: 
  *   either import them into scope `explicitly` or 
  *   define them `implicitly` in the companion object (preferred?)
  * 
  * Here's how to import them into scope `explicitly`:
  * {{{
  * import Monoid.{IntAdditionMonoid,StringConcatenationMonoid}
  * import FoldLeft.FoldLeftList
  *   //Create an implicit reference to the object inside the Monoid companion object
  *   implicit val intAddition = IntAdditionMonoid
  *   implicit val stringConcat = StringConcatenationMonoid
  *   implicit val foldLeftList = FoldLeftList
  * }}}
  */
  // def implicitly[T](t:T):T=t lets you pull out the implicit reference in that scope.
  implicit val intAddition = implicitly[Monoid[Int]]
  implicit val stringConcat = implicitly[Monoid[String]]
  
  val intMultiplication: Monoid[Int] = new Monoid[Int] {
    override def mappend(l: Int, r: Int): Int = l * r
    val mzero: Int = 1
  }
  
  /** `sum` function also generalized on List: 
    * So instead of taking a List[T], sum will take any higher kinded type of M[T]. (List,Option,Tree,etc.)
    * The `M[_]` indicates its a type constructor, that needs a type applied to it first. 
    * The Goal is to go from:
    * def sum(xs: List[Int]): Int = xs.foldLeft(0) { _ + _ }
    * to:
    * def sum[M[_]: FoldLeft, T: Monoid](xs: M[T])
    * {{{
    *   //Monoid
    *   //first try
    *   def sum[T](xs: List[T], m: Monoid[T]): T = ...
    *   //second try
    *   def sum[T](xs: List[T])(implicit m: Monoid[T]): T = FoldLeftList.foldLeft(xs, m.mzero, m.mappend)
    *   //third try - using context bounds
    *   def sum[T:Monoid](xs: List[T]): T = {
    *     val m = implicitly[Monoid[T]]
    *     FoldLeftList.foldLeft(xs, m.mzero, m.mappend) 
    *   }
    *   //FoldLeft
    *   //forth try
    *   def sum[M[_], T](xs: M[T])(implicit m: Monoid[T], fl: FoldLeft[M]): T = fl.foldLeft(xs, m.mzero, m.mappend)
    * }}}
    */
  def sum[M[_]: FoldLeft, T: Monoid](xs: M[T]): T = {
    val m = implicitly[Monoid[T]]
    val fl = implicitly[FoldLeft[M]]
    fl.foldLeft(xs, m.mzero, m.mappend)
  }//sum: [M[_], A](xs: M[A])(implicit evidence$1: FoldLeft[M], implicit evidence$2: Monoid[A])A
  
  
  def p(a: Any) {println("###> " + a)}

  def main(args: Array[String]): Unit = {
    println("*" * 100)
    
    p(sum(List(1, 2, 3, 4)))
    p(sum(List("a","b","c")))
    // explicitly passing all the parameters to replace the implicit IntAdditionMonoid.
    p(sum(List(1, 2, 3, 4))(implicitly[FoldLeft[List]], intMultiplication))
    
    
    // Method Injection "Pimp my library" example:
    p("Method Injection")
    // Enrich all types that has an instance for Monoid
    def plus[A: Monoid](a: A, b: A): A = implicitly[Monoid[A]].mappend(a, b)
    p(plus(3,4))
    p(plus("a","b"))
    //using Identity trait
    implicit def toIdent[A](a: A): Identity[A] = new Identity[A] {
      val value = a
    }
    //inject plus to both Int and String with just one definition.
    p(3.plus(4))
    p("a".plus("b"))
     
    implicit def toMonoidOp[A: Monoid](a: A): MonoidOp[A] = new MonoidOp[A] {
      val F = implicitly[Monoid[A]]
      val value = a
    }
    //inject |+| to both Int and String with just one definition.
    p(3 |+| 4)
    p("a" |+| "b")
    
    implicit def toMA[M[_], A](ma: M[A]): MA[M, A] = new MA[M, A] {
      val value: M[A] = ma
    }
    //Moving the sum function to MA and providing this implicit conversion for better syntax
    p(List(1, 2, 3, 4).summ)
    p(List("a", "b", "c").summ)
    
    println("*" * 100)
  }


}
