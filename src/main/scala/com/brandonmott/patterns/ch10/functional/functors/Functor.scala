package com.brandonmott.patterns.ch10.functional.functors

trait Functor[F[_]] { // F[_] uses the Higher-kinded Type feature 
  def map[T, Y](l: F[T])(f: T => Y): F[Y]
}
