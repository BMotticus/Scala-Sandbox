// Using for Comps 
/** 
  * This function checks if a number is `prime`. 
  *   A natural number is a prime number if it has exactly two 
  *   positive divisors, 1 and the number itself.
  * @param n - a natural number greater than 2 
  * @return - returns true if `n` is a prime number
  */
def isPrime(n: Int): Boolean = (2 until n) forall (n % _ != 0)
                                                //> isPrime: (n: Int)Boolean
val n = 7                                       //> n  : Int = 7
  
(1 until n) flatMap (i=>
  (1 until i) map (j => (i,j))) filter (pair =>
    isPrime(pair._1 + pair._2))                 //> res0: scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((2,1), (3,2
                                                //| ), (4,1), (4,3), (5,2), (6,1), (6,5))
 //easier way
 for{
   i <- 1 until n
   j <- 1 until i
   if isPrime(i + j)
 } yield (i, j)                                 //> res1: scala.collection.immutable.IndexedSeq[(Int, Int)] = Vector((2,1), (3,2
                                                //| ), (4,1), (4,3), (5,2), (6,1), (6,5))
