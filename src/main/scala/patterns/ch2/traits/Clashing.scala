package patterns.ch2.traits

/**
  * @Clashing_traits Sometimes the traits used to mixin have methods with identical signatures
  *  Here's the different ways traits can clash:
  *  1: Same signatures and return types traits
  *  2: Same signatures and different return types traits
  *  3: Same signatures and return types mixins
  *  4: Same signatures and different return types mixins
  */

/** @Clash_1: `Same signatures and return types traits`
  Here we mix two traits into a class and their declaration of a method is identical
  Possible Solution is to just implement the method once.
 */
trait FormalGreeting {def hello(): String}
trait InformalGreeting {def hello(): String}

class Greeter extends FormalGreeting with InformalGreeting {
  override def hello(): String = "Good morning, sir/madam!"
}

object GreeterUser extends App {
  val greeter = new Greeter()
  println(greeter.hello())
}

/** @Clash_2: `Same signatures and different return types traits`
  This clash would happen if `FormalGreeting` and `InformalGreeting` had the following method:
{{{
trait FormalGreeting {
  def getTime(): String
}
trait InformalGreeting {
  def getTime(): Int
}
}}}
  The compiler will not allow us to implement the same method twice in the Greeter class,
  Scala prevents such things from happening.
 */

  
/** @Clash_3: `Same signatures and return types mixins`
  * @Note that a MIXIN is just a trait that has some code implemented inside. 
  * This means you do not have to implement the methods inside the class that are already implemented.
  * 
  * This problem does not exist when input parameters to the methods differ either by type or by count since this is a new signature.
  */
trait AA {
  def hello(): String = "Hello, I am trait AA!"  //mixin
}
trait BB {
  def hello(): String = "Hello, I am trait BB!"  //mixin
}

/**
  * <b>Clashing methods is a problem in multiple inheritances</b>, we are forced to pick one of the available methods. 
  * Here is a possible fix inside the Clashing object:
  */
object Clashing extends App with AA with BB {
  override def hello(): String = super[AA].hello()
  println(hello())
}
/** The `super` notation
  What would happen if in the preceding example, instead of super[AA].hello(), we do the following: 
    {{{override def hello(): String = super.hello()}}} 
  Which hello method will be called and why? 
  In the current case, it will be the one in the BB trait and the output will be Hello, I am trait BB! 
  This depends on `linearization` in Scala, and we will be looking into this later in this chapter.
 */

/** @Clash_4: `Same signatures and different return types mixins`
  * The problem would occur if we have these two methods in our traits
  * {{{
        def value(a: Int): Int = a // in trait AA 
        def value(a: Int): String = a.toString // in trait BB
  * }}}
  * If we override the value method in the AA trait we would get the error: 
  *   `overriding method value in trait BB of type (a: Int)String; method value has incompatible type`
  * If we override the value method in the BB trait, the error will change respectively.
  * If we try to override both methods we would get a "method value is defined twice " error.
  * Scala prevents us from doing things for sake of avoiding problems in multiple inheritance.
  */

/** @Solution
  * For the sake of completeness, if you face a similar issue, there is a workaround 
  * (by sacrificing the mixin functionality). It will look like the following:
  */
trait CC {
  def value(a: Int): Int = a
}

trait DD {
  def value(a: Int): String = a.toString
}

/**
  * This example uses traits as `collaborators`, but it also loses the fact that 
  * the class that uses them is also an instance of the trait type, which can be useful for other operations as well.
  */
object Collaborators extends App{
  val c = new CC {}
  val d = new DD {}
  println(s"c.value: ${c.value(10)}")
  println(s"d.value: ${d.value(10)}")
}
