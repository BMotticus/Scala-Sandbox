package com.brandonmott.patterns.ch10.functional.monoids

/** 
  * A [[Monoid]] is a `purely algebraic structure`, which means that it is defined only by its `algebra`. 
  * All [[Monoid]]s must conform to the so called `Monoid laws`.
  * 
  * <br><b>The [[Monoid]] laws:</b>
  * <br>• A Monoid contains a `T type`.
  * <br>• A Monoid contains one `associative binary operation`.
  * <br>• A structure must have an `identity element` — zero.
  * <br>
  * [[Monoid]] is a type along with an `ASSOCIATIVE BINARY OPERATION` over it, which also has an `IDENTITY ELEMENT`.
  */
trait Monoid[T] {
  /**
    * Monoid's `ASSOCIATIVE BINARY OPERATION`:
    *   This means that for any x, y, and z of the T type, the following is true: 
    *     {{{
    *       op(op(x, y), z) == op(x, op(y, z))
    *     }}}
    * @param l left element
    * @param r right element
    * @return result of the operation
    */
  def op(l: T, r: T): T

  /**
    * Monoid's `IDENTITY ELEMENT`:
    *   This element is characterized by the fact that the previous operation will always return the other element: 
    *     {{{
    *       `op(x, zero) == x and op(zero, x) == x`
    *     }}}
    * @return the `identity element` of T type
    */
  def zero: T
}

