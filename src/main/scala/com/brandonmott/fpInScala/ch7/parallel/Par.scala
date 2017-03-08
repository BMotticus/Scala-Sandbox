package com.brandonmott.fpInScala.ch7.parallel

import java.util.concurrent._
import language.implicitConversions


/** [[Par]] is a Purely functional library for creating parallel and asynchronous computations:
  *   - it doesn’t need to know how to actually implement the parallelism.
  *   - is a description of a parallel computation that gets interpreted at a later time by something like the get/run function.
  *   - is a first-class program that we can run.
  */
trait Par {
  /** [[Par]] is represented by a function that needs an [[ExecutorService]] 
    * By returning a [[Future]] you defer the decisions a future makes to the caller of `run` */
  type Par[A] = ExecutorService => Future[A]
  
  /** `map2` combines the results of two parallel computations with a binary function. */
  def map2[A,B,C](a: Par[A], b: Par[B])(f: (A,B) => C): Par[C] 
  
  /** `fork` takes an "unevaluated" Par and “marks” it for concurrent evaluation. 
    * The evaluation won’t actually occur until forced by `run` */
  def fork[A](a: => Par[A]): Par[A] 

  /** `unit` promotes a constant value to a parallel computation */
  def unit[A](a: A): Par[A]

  /** `lazyUnit` wraps its "unevaluated" argument in a Par and marks it for concurrent evaluation.
    * This is an example of a "derived combinator", as opposed to a "primitive combinator" like `unit` */
  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))
  
  /** `run` extracts a value from a Par by actually performing the computation. 
    * Renamed the `get` function to `run` because `Par` is a 'first-class program' that we can "run" */
  def run[A](es: ExecutorService)(a: Par[A]): Future[A] =  a(es)
}

object BasicPar {
  /** By returning a [[Future]] you defer the decisions a [[Future]] makes to the caller of `run` */
  type Par[A] = ExecutorService => Future[A]
  
  def unit[A](a: A): Par[A] = (es: ExecutorService) => UnitFuture(a)

  /** `lazyUnit` wraps its "unevaluated" argument in a Par and marks it for concurrent evaluation.
    * This is an example of a "derived combinator", as opposed to a "primitive combinator" like `unit` */
  def lazyUnit[A](a: => A): Par[A] = fork(unit(a))

  /** `run` extracts a value from a Par by actually performing the computation. 
    * Renamed the `get` function to `run` because `Par` is a 'first-class program' that we can "run" */
  def run[A](es: ExecutorService)(a: Par[A]): Future[A] = a(es)

  /** `UnitFuture` is a simple implementation of [[Future]] that just "wraps a constant value".
    * It doesn’t use the [[ExecutorService]] at all, It’s always done and can’t be cancelled.
    * Its `get` method simply returns the value that we gave it. */
  case class UnitFuture[A](get: A) extends Future[A] {
    def isDone: Boolean = true
    def get(timeout: Long, units: TimeUnit): A = get
    def isCancelled: Boolean = false
    def cancel(mayInterruptIfRunning: Boolean): Boolean = false
  }

  /** Evaluating of separate logical threads: `fork` is the sole function for controlling parallelism.
    * with `map2` can always do {{{ fork(map2(a,b)(f)) }}} if we want the evaluation of `f` to occur in a separate thread. */
  
  /** This `map2` does NOT respect timeouts. 
    * It simply passes the ExecutorService on to both Par values, 
    * waits for the results of the Futures af and bf, applies f to them, and wraps them in a UnitFuture. */
  def map2Unit[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    (es: ExecutorService) => {
      val aF = a(es)
      val bF = b(es)
      UnitFuture(f(aF.get, bF.get))
    }

  /** A new [[Future]] implementation that respects the timeout: 
    * This implementation will not prevent repeated evaluation if multiple threads call `get` in parallel. 
    * We could prevent this using synchronization, but it isn't needed for our purposes here 
    * (also, repeated evaluation of pure values won't affect results). */
  case class Map2Future[A, B, C](a: Future[A], b: Future[B], f: (A,B) => C) extends Future[C] {
    /** the result of `map2` */
    @volatile var cache: Option[C] = None
    def isDone: Boolean = cache.isDefined
    def isCancelled: Boolean = a.isCancelled || b.isCancelled
    def cancel(mayInterruptIfRunning: Boolean): Boolean = a.cancel(mayInterruptIfRunning) || b.cancel(mayInterruptIfRunning)
    /** No timeout provided */
    def get: C = compute(Long.MaxValue)
    /** convert the timeout to nanoseconds and compute */
    def get(timeout: Long, units: TimeUnit): C = compute(TimeUnit.NANOSECONDS.convert(timeout,units))
    /** Step by step computing 2 futures the contract of timeouts on Future
      * @param nanos - the Nanoseconds of time allocated for evaluating both futures. */
    private def compute(nanos: Long): C = cache match {
      case Some(c) => c
      case None => {
        //get nanos when starting to evaluate `A`
        val startA = System.nanoTime()
        //calling [[Future.get]] pass in timeout `nanos`
        val resA = a.get(nanos, TimeUnit.NANOSECONDS)
        //get nanos when evaluation of `A` is done
        val stopA = System.nanoTime()
        //get the total nanos spent evaluating `A`
        val timeOnA = stopA - startA
        //Subtract the total nanos from evaluating `A` from `nanos`
        val resB = b.get(nanos - timeOnA, TimeUnit.NANOSECONDS)
        //map the results using funtion `f: (A,B) => C`
        val resC = f(resA, resB)
        //store the results in the cache variable
        cache = Some(resC)
        //return the result `C`
        resC  
      }
    }
  }
  
  /** This version respects timeouts. See `Map2Future` above: 
    * We’d need a new Future implementation that records the amount of time spent evaluating one future, 
    * and then subtracts that time from the available time allocated for evaluating the other future. */
  def map2[A, B, C](a: Par[A], b: Par[B])(f: (A, B) => C): Par[C] =
    es => {
      val (aF, bF) = (a(es), b(es))
      Map2Future(aF, bF, f)
    }
  
  /** `fork` be the sole function in the API for controlling parallelism. */

  /** This is the simplest and most natural implementation of `fork`.
    * Main problem is the outer Callable will block waiting for the “inner” task to complete.
    * Since this blocking occupies a thread in our thread pool, it ends up using two threads when one should suffice. 
    * This is a symptom of a more serious problem */
  def fork[A](a: => Par[A]): Par[A] =
    es => es.submit(new Callable[A] {
      override def call() = a(es).get
    })
  /** converts an A => B to an A => Par[B] by forking a parallel computation to produce the result. */
  def asyncF[A, B](f: A => B): A => Par[B] = a => lazyUnit(f(a))

  /* Gives us infix syntax for `Par`. */
  implicit def toParOps[A](p: Par[A]): ParOps[A] = new ParOps(p)
  class ParOps[A](p: Par[A]) {}

    /** The only other combinator we have that allows us to manipulate the value of a Par in any way is `map2` */
//  def sortPar(parList: Par[List[Int]]): Par[List[Int]] = map2(parList, unit(()))((a, _) => a.sorted)

  /** Generalized how we used map2 in sortPar, 
    * Now we can “lift” any function of type A => B to become a function that takes Par[A] and returns Par[B] */
  def map[A,B](a: Par[A])(f: A => B): Par[B] =
    map2(a,unit(()))((a,_) => f(a))

  /** using `map` to "lift" a function of type A => B to a function of type Par[A] => Par[B] */
  def sortPar(parList: Par[List[Int]]): Par[List[Int]] = 
    map(parList)(_.sorted)


  def sequence[A](ps: List[Par[A]]): Par[List[A]] =
    ps.foldRight[Par[List[A]]](unit(List()))((h, acc) => map2(h, acc)(_ :: _))

  /** This implementation forks the recursive step off to a new logical thread, making it effectively tail-recursive. 
    * However, we are constructing a right-nested parallel program, and we can get better performance by 
    * dividing the list in half, and running both halves in parallel. See `sequenceBalanced` below. */
  def sequence_2[A](ps: List[Par[A]]): Par[List[A]] =
    ps match {
      case Nil => unit(List())
      case h :: t => map2(h, fork(sequence_2(t)) )(_ :: _)
    }

  /** We define `sequenceBalanced` using `IndexedSeq`, which provides an efficient function for splitting the sequence in half. */
  def sequenceBalanced[A](as: IndexedSeq[Par[A]]): Par[IndexedSeq[A]] = fork {
    if (as.isEmpty) unit(Vector())
    else if (as.length == 1) map(as.head)(a => Vector(a))
    else {
      val (l, r) = as.splitAt(as.length / 2)
      map2(sequenceBalanced(l), sequenceBalanced(r))(_ ++ _)
    }
  }

  def sequence_3[A](as: List[Par[A]]): Par[List[A]] =
    map(sequenceBalanced(as.toIndexedSeq))(_.toList)

  /** `parMap` needs to combine N parallel computations.
    * Note that we’ve wrapped our implementation in a call to `fork`. With this implementation,
    * parMap will return immediately, even for a huge input list. 
    * 
    * When we later call run, it will fork a single asynchronous computation which itself spawns N parallel computations,
    * and then waits for these computations to finish, collecting their results into a list */
  def parMap[A, B](ps: List[A])(f: A => B): Par[List[B]] = fork {
    val fbs: List[Par[B]] = ps.map(asyncF(f))
    sequence(fbs)
  }
  
  //TODO
  /** filter's a list of elements in parallel */
  def parFilter[A](as: List[A])(f: A => Boolean): Par[List[A]] = ???
  
  
  
  
}