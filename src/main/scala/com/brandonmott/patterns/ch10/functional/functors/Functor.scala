package com.brandonmott.patterns.ch10.functional.functors

/**
  * Functor created by Brandon Mott on 10/16/16.
  */
trait Functor[F[_]] { // F[_] uses the Higher-kinded Type feature 
  def map[T, Y](l: F[T])(f: T => Y): F[Y]
}
