package com.brandonmott.patterns.ch3.unification

/**
  * We can assign any function to a variable and then call it as if it was a function (from invoking its `apply` method)
  * 
  * FunctionLiterals created by Brandon Mott on 8/4/16.
  */
class FunctionLiterals {
  val sum = (a: Int, b: Int) => a + b  //> sum field is actually assigned a function
  val product = (a: Int, b: Int) => a * b //> product field is also assigned a function
  /**
    * Functions can also be passed as parameters to other methods.
    * @param f - is a function parameter that takes a type (Int,Int) and returns Int. Its type signature: `(Int,Int) => Int`
    * @param a - the first value in the function parameter of type Int
    * @param b - the second value in the function parameter of type Int
    * @return - the result of the function
    */
  def runOperation(f: (Int, Int) => Int, a: Int, b: Int): Int = f(a, b)
  
}

object FunctionLiterals extends App {
  val obj = new FunctionLiterals
  println(s"'Example of sum(3,9)' => 3 + 9 = ${obj.sum(3,9)}")
  println(s"'Example of product(3,9)' => 3 * 9 = ${obj.product(3,9)}")
  println(s"'Example of runOperation(obj.sum,3,9)' => 3 + 9 = ${obj.sum(3,9)}")
  println(s"'Example of runOperation(obj.product,3,9)' => 3 * 9 = ${obj.product(3,9)}")
  println(s"'Another example of runOperation(Math.max, 10, 20)' => Math.max(10, 20) = ${Math.max(10, 20)}")
}


