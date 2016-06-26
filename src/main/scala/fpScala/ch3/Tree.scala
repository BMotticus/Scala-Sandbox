package fpScala.ch3


/** 
    `ADT` means "algebraic data type" sometimes referred to as "abstract data type"
    
    @ADT is just a data type defined by 1 or more _data constructors_, each of which may contain 0 or more arguements.

    @data-type is the `sum` or `union` of its _data constructors_, and each data constructor is 
    the `product` of its arguments, hence the name "algebraic data type".
  
    "Algebraic data types" can be used to define other data structures
*/

/* binary tree data structure */

sealed trait Tree[+A]  //`Tree` data type, `parameterized` on a type, `A`

/**
* @Leaf is a `Tree` _data constructor_ representing the value at the end of a Branch
*/
case class Leaf[A](value: A) extends Tree[A]

/**
* @Branch is a `Tree` _data constructor_ representing the Branch to 2 more Tree data structures
*/
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A] 

object Tree{ //companion object
//Nodes are leaves and branches
  
/**  Counts the number of nodes
  * @t Tree of nodes 
  */
  def size[A](t: Tree[A]):Int = t match {
    case Leaf(v) => 1
    case Branch(l,r) => 1 + size(l) + size(r) 
  }
  
  /**
   * We're using the method `max` that exists on all `Int` values rather than an explicit `if` expression.
   * Note how similar the implementation is to `size`. We'll abstract out the common pattern in a later exercise. 
   */
  def maximum(t: Tree[Int]):Int = t match {
    case Leaf(v) => v
    case Branch(l,r) => maximum(l) max maximum(r)
  }

  /**
  * Again, note how similar the implementation is to `size` and `maximum`. 
  */
  def depth[A](t: Tree[A]):Int = t match {
    case Leaf(v) => 0
    case Branch(l,r) => 1 + (depth(l) max depth(r)) 
  }

  /**
  * Use the data constructor on the right hand side of the match to maintain structure 
  */
  def map[A,B](t: Tree[A])(f: A => B): Tree[B] = t match {
    case Leaf(v) => Leaf(f(v))
    case Branch(l,r) => Branch(map(l)(f), map(r)(f)) 
  }
  
  /** 
    * Like `foldRight` for lists, `fold` receives a "handler" for each of the data constructors of the type, and recursively
    * accumulates some value using these handlers. As with `foldRight`, `fold(t)(Leaf(_))(Branch(_,_)) == t`, and we can use
    * this function to implement just about any recursive function that would otherwise be defined by pattern matching.
    */
  def fold[A,B](t: Tree[A])(f: A => B)(g: (B,B) => B): B = t match {
    case Leaf(v) => f(v)
    case Branch(l,r) => g(fold(l)(f)(g), fold(r)(f)(g))
  }
  
  def sizeViaFold[A](t: Tree[A]): Int = fold(t)(a => 1)(1 + _ + _)
  
  def maximumViaFold[A](t: Tree[Int]): Int = fold(t)(a => a)(_ max _)
  
  def depthViaFold[A](t: Tree[A]): Int = fold(t)(a => 0)((b1,b2) => 1 + (b1 max b2))

  def mapViaFold[A,B](t: Tree[A])(f: A => B): Tree[B] = fold(t)( a => Leaf(f(a)):Tree[B] )( Branch(_,_) )
  /**
  Note the type annotation required on the expression `Leaf(f(a))`. Without this annotation, we get an error like this: 
  
  type mismatch;
    found   : fpinscala.datastructures.Branch[B]
    required: fpinscala.datastructures.Leaf[B]
       fold(t)(a => Leaf(f(a)))(Branch(_,_))
                                      ^  
  
  This error is an unfortunate consequence of Scala using subtyping to encode algebraic data types. Without the
  annotation, the result type of the fold gets inferred as `Leaf[B]` and it is then expected that the second argument
  to `fold` will return `Leaf[B]`, which it doesn't (it returns `Branch[B]`). Really, we'd prefer Scala to
  infer `Tree[B]` as the result type in both cases. When working with algebraic data types in Scala, it's somewhat
  common to define helper functions that simply call the corresponding data constructors but give the less specific
  result type:
    
    def leaf[A](a: A): Tree[A] = Leaf(a)
    def branch[A](l: Tree[A], r: Tree[A]): Tree[A] = Branch(l, r)
  
  */
  
}


object ex2 extends App {
  
}