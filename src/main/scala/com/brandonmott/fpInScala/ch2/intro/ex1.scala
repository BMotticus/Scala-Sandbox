package com.brandonmott.fpInScala.ch2.intro

/**
 * Created by brandonmott1 on 1/10/16.
 */ //Exercise 1
object ex1 extends App{

  def findFirst(ss: Array[String], key: String): Int = {
    def go(i:Int): Int = {
      if(ss.length <= i) -1
      else if(ss(i) == key) i
      else go(i + 1)
    }
    go(0)
  }
  
  /*def findFirst[A](as: Array[A], p: A => Boolean): Int = {
    @annotation.tailrec
    def loop(n: Int): Int =
      if (n >= as.length) -1
      // If the function `p` matches the current element,
      // we've found a match and we return its index in the array.
      else if (p(as(n))) n
      else loop(n + 1)

    loop(0)
  }*/

  val arr = Array("foo","blah","bar","hola","home","me")
  val name = "foo"
  println(s"findFirst Array length is ${arr.length - 1}, found ${name} at ${findFirst(arr,name)}")

  def fib(x: Int): Int = {
    def go(x: Int, a: Int, b: Int):Int ={
      if(x == 0) a
      else go(x-1, b, a+b)
    }
    go(x, 0, 1)
  }
  
  println("The first 25 fibonacci number's are: ")
  0 to 25 foreach ( x => print(fib(x) + ", "))
  println()
}
//Exercise 2
object polymorphic extends App {
  
  def isSorted[A](as:Array[A], ordered: (A,A) => Boolean):Boolean ={
    def go[A]( a: Array[A], f: (A,A) => Boolean, x:Int):Boolean = {
      if (a.length - 1 == x) true
      else if (f(a(x),a(x+1))) go(a,f, x+1)
      else false
    }
    go(as,ordered,0)
  }
  
  /* 
    // Exercise 2: Implement a polymorphic function to check whether
    // an `Array[A]` is sorted
    def isSorted[A](as: Array[A], gt: (A,A) => Boolean): Boolean = {
      @annotation.tailrec
      def go(n: Int): Boolean =
        if (n >= as.length-1) true
        else if (gt(as(n), as(n+1))) false
        else go(n+1)
  
      go(0)
    }
   */
  
  def lowToHigh(a:Int, b:Int) = a < b
  val res = isSorted(Array(1,2,3,4,5,6,7), lowToHigh)
  
  println(res)
}
//Exercise 3
object curry {
  
  def curry[A,B,C](f: (A, B) => C): A => (B => C) = {
    a => b => f(a, b)
  }
  
  def uncurry[A,B,C](f: A => (B => C)): (A, B) => C = {
    (a,b) => f(a)(b)
  }
  
  def compose[A,B,C](f: B => C, g: A => B): A => C = {
    a => f(g(a))
  }    
}

//Exercise 4
object aggregate extends App {
  import scala.collection.immutable._
  println(List('a', 'b', 'c').aggregate(0)({ (sum, ch) =>
    println(s" $sum + ${ch.toInt} = ${sum + ch.toInt}")
    sum + ch.toInt
  }, { (p1, p2) =>
    println(s"$p1 + $p2 = ${p1 + p2}")
    p1 + p2
  }))
}