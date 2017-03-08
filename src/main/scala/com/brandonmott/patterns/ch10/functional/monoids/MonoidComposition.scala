package com.brandonmott.patterns.ch10.functional.monoids

object MonoidComposition extends App {
  val numbers = Array(1, 2, 3, 4, 5, 6)
  
  /** This [[Monoid]] of type (Int,Int) calculates the `sum` and the `factorial` of the numbers given to it. */
  val sumAndProduct: Monoid[(Int,Int)] = MonoidOperations.compose(intAddition, intMultiplication)
  System.out.println(s"The sum and product is: ${MonoidOperations.balancedFold(numbers, sumAndProduct)(i => (i, i))}")

  /** We can also efficiently calculate the `mean` of all items in a list — we just need to use the `intAddition` [[Monoid]] twice and 
    * `map` the [[numbers]] to `(number, 1)` in order to have the count together with the `sum`. */
  val intAddTwice = MonoidOperations.compose(intAddition, intAddition)
  val meanCount = MonoidOperations.balancedFold(numbers, intAddTwice)(i => (i, 1))
  System.out.println(s"The mean is: ${meanCount._1 / meanCount._2}") //result is rounded to 3, actual is 3.5 

  /** In machine learning, we might need to extract the features from some text. 
    * Then each feature will be weighted using a `coefficient` and a number equal to the number of times we've seen it (the `count`). 
    * Let's try and get to a [[Monoid]] that can be used to `fold` a collection and give us what we need — the `count` of each feature. */
  
}
