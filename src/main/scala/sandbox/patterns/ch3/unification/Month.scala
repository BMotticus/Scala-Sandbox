package sandbox.patterns.ch3.unification

/** 
  * `Algebraic data types` are just `composite types that combine other existing types` or `just represent some new ones`.
  * They have only data and do not contain any functionality on top of this data as normal classes would.
  */

/** `Sum ADTs`
  * `Sum algebraic data types` are the ones in which we can simply `enumerate all the possible values of a type` and 
  * `provide a separate constructor for each value`.
  */
  
/**
  * Month is a ADT for the 12 months. Each month has it's own constructor.
  */
sealed abstract trait Month  //The trait is `sealed` to warn if the pattern match is incomplete. 
case object January extends Month
case object February extends Month
case object March extends Month
case object April extends Month
case object May extends Month
case object June extends Month
case object July extends Month
case object August extends Month
case object September extends Month
case object October extends Month
case object November extends Month
case object December extends Month

object MonthDemo extends App {
  val month: Month = February
  println(s"The current month is: $month")
}

/** `Product ADTs`
  * In `product algebraic data types`, we cannot enumerate all the possible values. They are usually too many manually write them. 
  * We cannot provide a separate constructor for each separate value. For the `product ADTs`, we have one constructor for different values.
  */
  
/**   
  * `RGB` represents different colors. 
  * `RGB` combines the different values of the main colors (red, green, and blue) in order to represent other colors.
  * each main color value is between 0 and 255
  */
sealed case class RGB(red: Int, green: Int, blue: Int)

object RGBDemo extends App {
  val magenta = RGB(255, 0, 255)
  println(s"Magenta in RGB is: $magenta")
}

/** Hybrid ADTs 
  * `Hybrid algebraic data types` represent `a combination of the sum and product` ADTs. 
  * `Hybrid ADTs` have `specific value constructors`, but these value constructors also provide parameters in order to wrap other types.
  */
  
/**
   The following example shows a `sum ADT` because we have the `Circle` and `Rectangle` specific value constructors. 
   Also, we have a `product ADT` because the constructors take extra parameters.  
 */
 
/** `Point` represents a `pair of numerical coordinates` on the `Cartesian Coordinate System`
  * When drawing our a `Shape`, we need to know their position.
  * @param x - point on the x axis
  * @param y - point on the y axis
  */
case class Point(x: Double, y: Double)

/** 
  * `Shape` represents different drawable shapes  
  */
sealed abstract trait Shape

/** 
  * 
  * @param centre
  * @param radius
  */
case class Circle(centre: Point, radius: Double) extends Shape

/**
  * 
  * @param topLeft
  * @param height
  * @param width
  */
case class Rectangle(topLeft: Point, height: Double, width: Double) extends Shape

/*
  After all of the preceding examples, it is obvious that class hierarchies and ADTs are unified and look like the same thing. 
  This adds a high level of flexibility in the language and makes modeling easier than in other functional programming languages.
 */
//TODO: Pattern Match for product ADTs

