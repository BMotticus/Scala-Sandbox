package com.brandonmott.patterns.ch10.functional.monoids

/**
  * To show how useful monoids are with collections that support the foldLeft and foldRight functions, 
  * let's take a look at the standard Scala list and the declarations of these two functions:
  * {{{
  *   def foldLeft[B](z: B)(f: (B, A) => B): B
  *   def foldRight[B](z: B)(f: (A, B) => B): B
  * }}}
  *
  * Usually, the z parameter in these two functions is called the zero value. 
  * So if A and B are of the same type, we will end up with the following:
  * {{{
  *   def foldLeft[A](z: A)(f: (A, A) => A): A
  *   def foldRight[A](z: A)(f: (A, A) => A): A
  * }}}
  *
  * These functions are exactly monoid rules.
  * This example uses the monoids created in the package object.
  */
object MonoidFolding extends App {
  val strings = List("This is\n", "a list of\n", "strings!")
  val numbers = List(1, 2, 3, 4, 5, 6)
  /**
    * Note: it doesn't actually matter for the final result whether we use `foldLeft` or `foldRight` 
    * because our monoids have an ``associative` operation`. It does, however, matter in terms of performance.
    */
  //Using String Concatenation
  System.out.println(s"Left folded:\n ${strings.foldLeft(stringConcatenation.zero)(stringConcatenation.op)}")
  //Using String Concatenation
  System.out.println(s"Right folded:\n ${strings.foldRight(stringConcatenation.zero)(stringConcatenation.op)}")
  //Using Integer Multiplication
  System.out.println(s"6! is: ${numbers.foldLeft(intMultiplication.zero)(intMultiplication.op)}")
}

/**
  * In `fold`, we made the A and B types to be the same in the `foldLeft` and `foldRight` functions.
  */
object MonoidFoldingGeneric extends App {
  val strings = List("This is\n", "a list of\n", "strings!")
  val numbers = List(1, 2, 3, 4, 5, 6)
  //Using the generic fold from MonoidOperations
  System.out.println(s"Left folded:\n ${MonoidOperations.fold(strings, stringConcatenation)}")
  System.out.println(s"Right folded:\n ${MonoidOperations.fold(strings, stringConcatenation)}")
  System.out.println(s"6! is: ${MonoidOperations.fold(numbers, intMultiplication)}")
}

/**
  * The fact that a [[Monoid]] operation is <b>`associative`</b> 
  * means that if we have to chain multiple operations, we could probably do it in parallel.
  *
  * For example, if we have the numbers 1, 2, 3, and 4 and wanted to find 4!, we can use what we used previously, 
  * which would end up being evaluated to the following:
  * {{{
  *   op(op(op(1, 2), 3), 4)
  * }}}
  *
  * The associativity, however, would allow us to do the following:
  * {{{
  *   op(op(1, 2), op(3, 4))
  * }}}
  *
  */
object MonoidBalancedFold extends App {
  val numbers = Array(1, 2, 3, 4)
  System.out.println(s"4! is: ${MonoidOperations.balancedFold(numbers, intMultiplication)(identity)}")
  
}