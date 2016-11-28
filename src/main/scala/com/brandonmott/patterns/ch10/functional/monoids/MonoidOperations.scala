package com.brandonmott.patterns.ch10.functional.monoids

/**
  * [[MonoidOperations]] has `generic functions` that preform `folding operations` on a sequence, using a [[Monoid]] and 
  * do different things depending on the [[Monoid]] operation.
  */
object MonoidOperations {
  /**
    * A generic fold operation that uses a Monoid.
    */
  def fold[T](list: List[T], m: Monoid[T]): T =
  list.foldLeft(m.zero)(m.op)

  /**
    * In `fold`, we made the A and B types to be the same in the `foldLeft` and `foldRight` functions. 
    *
    * However, we might build a different data structure with a different type, or 
    * our algorithm might rely on a different type that has a different monoid than the type of the list we have. 
    *
    * In order to support such a scenario, we have to add a possibility of `mapping` the type of the original list to a different type.
    *
    * This would give us the possibility of implementing even more complex operations on top of our lists using different types of monoids.
    */
  def foldMap[T,Y](list: List[T], m: Monoid[Y])(f: T => Y):Y = list.foldLeft(m.zero){
    case (t, y) => m.op(t, f(y)) //m must be type Y 
  }

  /**
    * Here the `nested operations` could be done independently and in parallel. This is also called <b>`balanced fold`</b>. 
    *
    * It is worth mentioning that we've used an `IndexedSeq` here, <b>as it will guarantee that getting elements by index will be efficient.</b> 
    *
    * Also, this code is not parallel but we've switched the order of the operations as we mentioned previously. 
    * {{{
    *   In the case of integers, it might not make much of a difference but for other types such as strings, it will improve the performance. 
    *   The reason is that strings are immutable and every concatenation will create a new string by allocating new space. 
    * }}}
    * So if we are simply going from the `left-hand side` to the `right-hand side`, 
    * we will be `allocating` more and more space and throwing away the intermediate results all the time.
    */
  def balancedFold[T, Y](list: IndexedSeq[T], m: Monoid[Y])(f: T => Y): Y =
  if (list.length == 0) {
    m.zero
  } else if (list.length == 1) {
    f(list(0))
  } else {
    val (left, right) = list.splitAt(list.length / 2) //split list in half
    m.op(balancedFold(left, m)(f), balancedFold(right, m)(f))
  }

  /** 
    * So far, we have seen some examples where a [[Monoid]] used to `improve efficiency` and `write generic functions`. 
    * They, however, are even more powerful. The reason is that they follow another useful rule:
    * {{{
    *   Monoids support composition; if A and B are monoids, then their "product" (A, B) is also a Monoid.
    * }}}
    * 
    * This would now allow us to simultaneously `apply multiple operations` using a [[Monoid]]. 
    * And we can `compose` even more and apply even more operations. 
    */
  def compose[T, Y](a: Monoid[T], b: Monoid[Y]): Monoid[(T, Y)] =
    new Monoid[(T, Y)] {
      val zero: (T, Y) = (a.zero, b.zero)
      override def op(l: (T, Y), r: (T, Y)): (T, Y) =
        (a.op(l._1, r._1), b.op(l._2, r._2))
    }
}
