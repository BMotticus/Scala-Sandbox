package com.brandonmott.patterns.ch2.traits


/** The Diamond problem
  * Here, both B and C extend A and then D extends B and C. Some ambiguities might arise from this. 
  * Let's say that there was a method that was originally defined in A, but both B and C override it. 
  * What would happen if D calls this method? Which one will it exactly call? */
trait A {
  def hello(): String = "Hello from A"
}

trait B extends A {
  override def hello(): String = "Hello from B"
}

trait C extends A {
  override def hello(): String = "Hello from C"
}

trait D extends B with C {}

object Diamond extends App with D {
  println(hello()) //Here is the output: Hello from C
}
/** If we change trait D to look like this: {{{ trait D extends B with C {} }}} The the output would be: Hello from B 
  * Even though the example is still ambiguous and error prone, we can tell which method will be exactly called. 
  * This is achieved using <b> linearization </b> 
  * Scala multiple inheritance limitations Multiple inheritance in Scala is achieved `using traits` and `it follows the rules of linearization`. 
  * In the inheritance hierarchy, if there is a trait that explicitly extends a class, the class that mixes in this trait must also be a subclass of the trait parent.
  * This means that when mixing in traits that extend classes, ``they must all have the same parent``. It is not possible to mix traits in, 
  * which define or declare methods with the same signatures but different return types. */