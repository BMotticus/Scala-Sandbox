package patterns

/**
  * Strategy helps us to define a set of algorithms, encapsulates each step, and selects one as appropriate. 
  * We need this ability to plug in an algorithm as needed. 
  * When we start applying a strategy, we really try to apply the Open/Closed principle (OCP).
  */
object basicMath {
  //these various strategy algorithms are functions
  def addThem(a: Int, b: Int) = a + b // algorithm 1
  def subtractThem(a: Int, b: Int) = a - b // algorithm 2
  def multiplyThem(a: Int, b: Int) = a * b // algorithm 3
}
/**
  * Created by brandonmott1 on 7/26/16.
  */
object strategyDesignPattern extends App{
  /**
    * this executes a simple math problem
    * @param f - the strategy algorithm
    * @param x - the first argument in the strategy algorithm
    * @param y - the second argument in the strategy algorithm
    * @return
    */
  def execute(f: (Int, Int) => Int, x: Int, y: Int) = f(x, y)

  println("Add: " + execute(basicMath.addThem, 3, 4))
  println("Subtract: " + execute(basicMath.subtractThem, 3, 4))
  println("Multiply: " + execute(basicMath.multiplyThem, 3, 4))
  
}
