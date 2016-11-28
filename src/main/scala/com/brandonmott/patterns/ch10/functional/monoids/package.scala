package com.brandonmott.patterns.ch10.functional

/** `Monoids in Real Life`
  * The `Monoid laws` are extremely simple but they give us great power to write polymorphic functions 
  * based just on the fact that monoids always conform to the same rules.
  */
package object monoids {
  /**
    * Let's look at `INTEGER ADDITION`:
    * • Our `type`: Int
    * • Our `associative operation`: add. It is indeed associative because ((1 + 2) + 3) == (1 + (2 + 3)).
    * • Our `identity element`: 0. It does nothing when added to another integer.
    */
  val intAddition: Monoid[Int] = new Monoid[Int] {
    val zero: Int = 0
    override def op(l: Int, r: Int): Int = l + r
  }

  /**
    * Let's look at `INTEGER MULTIPLICATION`:
    * • Our `type`: Int
    * • Our `associative operation`: multiply. It is indeed associative because ((1 * 2) * 3) == (1 * (2 * 3)).
    * • Our `identity element`: 1. any integer does nothing when multiplied by 1.
    */
  val intMultiplication: Monoid[Int] = new Monoid[Int] {
    val zero: Int = 1
    override def op(l: Int, r: Int): Int = l * r
  }

  /**
    * Let's look at `STRING CONCATENATION`:
    * • Our `type`: String
    * • Our `associative operation`: concatenate. It is associative because (("bra" + "nd") + "on") == ("bra" + ("nd" + "on")).
    * • Our `identity element`: an empty string. an empty string doesn't change a string.
    */
  val stringConcatenation: Monoid[String] = new Monoid[String] {
    val zero: String = ""
    override def op(l: String, r: String): String = l + r
  }
  
  
}
