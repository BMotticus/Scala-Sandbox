//> sets are unordered and don't contain duplicates
// input is the number of queens
// output would be set of solutions
def queens (n: Int): Set[List[Int]] = {
  def placeQueens(k: Int): Set[List[Int]] =
    if (k== 0) Set(List())
    else
      for {
        queens <- placeQueens(k-1)
        col <- 0 until n
        if isSafe(col, queens)
      } yield col :: queens
  placeQueens(n)

}                                               //> queens: (n: Int)Set[List[Int]]

def isSafe(col: Int, queens: List[Int]): Boolean = {
  val row = queens.length
  //use a zip with range
  val queensWithRow = (row -1 to 0 by -1) zip queens
  queensWithRow forall {
    //check if the current col is not the same / check the absolute difference between two cols must not be the same as two rows
    case (r,c) => col != c && math.abs(col -c) != row - r
  }
}                                               //> isSafe: (col: Int, queens: List[Int])Boolean

def show(queens: List[Int]) = {
  val lines =
    for(col <- queens.reverse)
      yield Vector.fill(queens.length)("* ").updated( col, "X ").mkString
  "\n" + (lines mkString "\n")
}                                               //> show: (queens: List[Int])String
//white square U+25fb   &#9723;  &#x25fb;
//black square U+25fc   &#9724;  &#x25fc;
//white queen  U+2654   &#9812;  &#x2654;
//black queen  U+265a   &#9818;  &#x265a;
//found at http://www.amp-what.com/

queens(4)                                       //> res0: Set[List[Int]] = Set(List(1, 3, 0, 2), List(2, 0, 3, 1))

/* output that looks like a 8 x 8 chess board */
queens(8) map show  




