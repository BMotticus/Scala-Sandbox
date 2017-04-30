package patterns.functional.monoids.syntax

import patterns.functional.monoids.{FoldLeft, Monoid}

/**
  * Enrich my library. inject `plus` to both Int and String with just one definition.
  */
trait Identity[A] {
  val value: A

  def plus(a2: A)(implicit m: Monoid[A]): A = m.mappend(value, a2)
}




