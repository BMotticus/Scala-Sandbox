package com.brandonmott.fpInScala.ch6.state

/**
  * Purely functional (state) random number generation
  * The key to recovering referential transparency is to make the state updates explicit.
  * 
  * Don’t update the state as a side effect, but simply return the new state along with
  * the value that we’re generating. Leaving the old state unmodified.
  * 
  * In effect, we separate the concern of `computing` what the next state is from the concern
  * of `communicating` the new state to the rest of the program.
  */
trait RNG {
  def nextInt: (Int, RNG)

  
}

object RNG {
/*
  Important Note on defining classes & methods inside objects: 
    You can define the class Simple and inside the RNG object, You will just have to change the imports to RNG._ to access the class. 
    You can define the methods in the RNG object but then you'll have to call the methods using the singleton object RNG.randonPair(rng1)
*/

  /**
    * We return the final state after generating the two
    * random numbers. This lets the caller generate
    * more random values using the new state.
    *
    * @return - Pair of random numbers (Int,Int) and the next RNG
    */
  def randomPair(rng: RNG): ((Int, Int), RNG) = {
    val (i1, rng2) = rng.nextInt
    val (i2, rng3) = rng2.nextInt // Note use of rng2 here.
    ((i1, i2), rng3)
  }

  /**
    * We need to be quite careful not to skew the generator.
    * Since `Int.Minvalue` is 1 smaller than `-(Int.MaxValue)`,
    * it suffices to increment the negative numbers by 1 and make them positive.
    * This maps Int.MinValue to Int.MaxValue and -1 to 0.
    *
    * @return generated random integer between 0 and Int.maxValue (inclusive)
    */
  def nonNegativeInt(r: RNG): (Int, RNG) = {
    val (i, rng) = r.nextInt
    (if (i < 0) -(i + 1) else i, rng)
  }


  /**
    * We generate an integer >= 0 and divide it by one higher than the
    * maximum. This is just one possible solution.
    *
    * @return generated Double between 0 and 1, not including 1
    */
  def double1(r: RNG): (Double, RNG) = {
    val (i, rng) = nonNegativeInt(r)
    (i / (Int.MaxValue.toDouble + 1), rng)
  }

  def intDouble(r: RNG): ((Int, Double), RNG) = {
    val (i, rng1) = r.nextInt
    val (d, rng2) = double1(rng1)
    ((i, d), rng2)
  }

  def doubleInt(r: RNG): ((Double, Int), RNG) = {
    val (i, rng1) = r.nextInt
    val (d, rng2) = double1(rng1)
    ((d, i), rng2)
  }

  def double3(r: RNG): ((Double, Double, Double), RNG) = {
    val (d1, rng1) = double1(r)
    val (d2, rng2) = double1(rng1)
    val (d3, rng3) = double1(rng2)
    ((d1, d2, d3), rng3)
  }

  /**
    * Use `recursion` to eliminate some of this duplication. This is a tail recursive solution
    *
    * @param count the length of the random integer list
    * @return generate list of integers 
    */
  def ints(count: Int)(r: RNG): (List[Int], RNG) = {
    @annotation.tailrec
    def init(list: List[Int], c: Int, rng: RNG): (List[Int], RNG) = {
      if (c == 0) (list, rng)
      else {
        val (i, next) = rng.nextInt
        init(i :: list, c - 1, next)
      }
    }
    init(List.empty[Int], count, r)
  }

  /**
    * A common pattern of the functions above: 
    * Each of our functions has a type of the form `RNG => (A, RNG)` for some type A.    
    *
    * Functions of type `RNG => (A, RNG)` are called `state actions` or `state transitions` because 
    * they transform RNG states from one to the next. 
    *
    * `State actions` can be combined using `combinators`, 
    * which are higher-order functions that pass the state from one action to the next `automatically`.
    *
    */
  
  /** Using Rand[A] type alias for `state actions`:
    * Make a `type alias` for the RNG `state action` data type
    * @tparam A the randomly generated type
    */
  type Rand[+A] = RNG => (A, RNG)

  /* Using the type alias Rand[A] */

  /** Turn RNG’s nextInt & double method's into values of the new `Rand[A]` type: */
  val int: Rand[Int] = _.nextInt
  val double: Rand[Double] = rng => double1(rng)

  /* We want to write combinators that let us combine Rand actions while avoiding explicitly passing along the RNG state. */

  /** a simple RNG state transition is the `unit action`, which passes the RNG state through without using it. 
    *
    * @return always returning a `constant value` rather than a random value:
    */
  def unit[A](a: A): Rand[A] = rng => (a, rng)

  /** Remember, ``Rand[A] is just a type alias`` for a function type RNG => (A, RNG), so this is just a kind of `function composition`: */
  def map[A, B](s: Rand[A])(f: A => B): Rand[B] =
    rng => {
      val (a, rng2) = s(rng)
      (f(a), rng2)
    }

  def nonNegativeEven(): Rand[Int] =
    map(nonNegativeInt)(x => x - x % 2)
  
  val _double: Rand[Double] = 
    map(nonNegativeInt)(_ / (Int.MaxValue.toDouble + 1))

  /*
  	Combining state actions 
      Unfortunately, map isn’t powerful enough to implement intDouble and doubleInt. 
      What we need is a new combinator map2 that can combine two RNG actions into one using a binary rather than unary function.
   */
  /**
    * This implementation of map2 passes the initial RNG to the first argument
    * and the resulting RNG to the second argument. It's not necessarily wrong
    * to do this the other way around, since the results are random anyway.
    * We could even pass the initial RNG to both `f` and `g`, but that might
    * have unexpected results. E.g. if both arguments are `RNG.int` then we would
    * always get two of the same `Int` in the result. When implementing functions
    * like this, it's important to consider how we would test them for
    * correctness.
    */
  def map2[A, B, C](ra: Rand[A], rb: Rand[B])(f: (A, B) => C): Rand[C] = 
    rng => {
      val (a, rng2) = ra(rng)
      val (b, rng3) = rb(rng2)
      (f(a,b), rng3) 
    } 
  //TODO: Continue on pg. 85 "6.4.1 Combining state actions"
}


/**
  * The following is a random number generator that uses the same algorithm as [[scala.util.Random]], 
  * which happens to be what’s called a `linear congruential generator`
  *
  * @param seed - an arbitrary seed value
  */
case class Simple(seed: Long) extends RNG {
  def nextInt: (Int, RNG) = {
    // & is bitwise AND. We use the current seed to generate a new seed.
    val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
    //The next state, which is an RNG instance created from the new seed.   
    val nextRNG = Simple(newSeed)
    // `>>>` is right binary shift with zero fill. The value n is the new pseudorandom  integer.
    val n = (newSeed >>> 16).toInt
    //The return value is a tuple containing both a pseudo - random integer and the next RNG state.
    (n, nextRNG)
  }
}

