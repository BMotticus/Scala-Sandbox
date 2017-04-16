package sandbox.patterns.ch10.functional.monoids

object BasicMonoidExamples extends App {
  
  // Monoid DEFINITIONS 
  // defined as an "instance", instead of as an object inside the Monoid companion object.
  val intAddition: Monoid[Int] = new Monoid[Int] {
    override def mappend(l: Int, r: Int): Int = l + r
    val mzero: Int = 0
  }
  
  val intMultiplication: Monoid[Int] = new Monoid[Int] {
    override def mappend(l: Int, r: Int): Int = l * r
    val mzero: Int = 1
  }
  
  val stringConcatenation: Monoid[String] = new Monoid[String] {
    override def mappend(l: String, r: String): String = l + r
    val mzero: String = ""
  }
  
  val strings = List("This is\n", "a list of\n", "strings!")
  val numberList = List(1, 2, 3, 4, 5, 6)
  
  //Using String Concatenation
  println(s"Left folded:\n ${strings.foldLeft(stringConcatenation.mzero)(stringConcatenation.mappend)}")
  
  //Using String Concatenation
  println(s"Right folded:\n ${strings.foldRight(stringConcatenation.mzero)(stringConcatenation.mappend)}")
  
  //Using Integer Multiplication
  println(s"6! is: ${numberList.foldLeft(intMultiplication.mzero)(intMultiplication.mappend)}")

  /** In `fold`, we made the A and B types to be the same in the `foldLeft` and `foldRight` functions. */
  
  //Using the generic fold from MonoidOperations
  println(s"Left folded:\n ${MonoidOperations.fold(strings, stringConcatenation)}")
  println(s"Right folded:\n ${MonoidOperations.fold(strings, stringConcatenation)}")
  println(s"6! is: ${MonoidOperations.fold(numberList, intMultiplication)}")

  val numbers = Array(1, 2, 3, 4, 5, 6)
  println(s"4! is: ${MonoidOperations.balancedFold(numbers, intMultiplication)(identity)}")

  //COMPOSITION
  /** 
    * This Monoid of type (Int,Int) calculates the `sum` and the `factorial` of the numbers given to it. 
    */
  val sumAndProduct: Monoid[(Int, Int)] = MonoidOperations.compose(intAddition, intMultiplication)
  println(s"The sum and product is: ${MonoidOperations.balancedFold(numbers, sumAndProduct)(i => (i, i))}")

  /** 
    * We can also efficiently calculate the `mean` of all items in a list — 
    *   we just need to use the intAddition Monoid twice and 
    * map the `numbers` to (number, 1) in order to have the count together with the `sum`. 
    * */
  val intAddTwice = MonoidOperations.compose(intAddition, intAddition)
  val meanCount = MonoidOperations.balancedFold(numbers, intAddTwice)(i => (i, 1))
  println(s"The mean is: ${meanCount._1 / meanCount._2}") //result is rounded to 3, actual is 3.5 

}

/** Notes from "Monoids in real life":
  * The declarations of `foldLeft` and `foldRight` functions:
  *   def foldLeft[B](z: B)(f: (B, A) => B): B
  *   def foldRight[B](z: B)(f: (A, B) => B): B 
  *
  * Note: the `z` parameter in these two functions is called the `ZERO VALUE`. 
  *
  * If `A` and `B` are of the SAME type `A`:
  *   def foldLeft[A](z: A)(f: (A, A) => A): A 
  *   def foldRight[A](z: A)(f: (A, A) => A): A 
  *
  * Then both functions are EXACTLY the Monoid rules
  *   foldLeft(m.mzero)(m.mappend) 
  *   foldRight(m.mzero)(m.mappend)
  *
  * Note about `foldLeft` & `foldRight`: 
  *   The final result is the SAME because Monoid's ASSOCIATIVE operation. 
  *   The performance is different.
  *
  * The fact that a Monoid operation is `associative`
  *   means that if we have to chain multiple operations, we could probably do it in parallel.
  *
  * For example, if we have the numbers 1, 2, 3, and 4 and wanted to find 4!, we can use what we used previously, 
  * which would end up being evaluated to the following:
  *   mappend(mappend(mappend(1, 2), 3), 4) 
  *
  * The associativity, would allow us to do the following:
  *   mappend(mappend(1, 2), mappend(3, 4)) 
  *
  * In machine learning, we might need to extract the features from some text. 
  * Then each feature will be weighted using a `coefficient` and 
  * a number equal to the number of times we've seen it (the `count`). 
  *
  * Let's try and get to a Monoid that can be used to `fold` a collection and give us what we need — 
  * the `count` of each feature. 
  */

