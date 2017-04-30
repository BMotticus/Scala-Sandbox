package coursera.reactive

/** An Observer
  * For a `View` define a Consolidator class that `observes` a list of bank accounts.
  * Consolidator is a Subscriber that would always be up to date with the `total balance` of all the bank accounts.
  * 
  * Consolidator created by Brandon Mott on 9/26/16.
  */
class Consolidator(observed: List[BankAccount]) extends Subscriber {
  
  /** As an `Initialization Action`,
    * Consolidator initially subscribes itself to all observed bank accounts. 
    */
  observed.foreach(_.subscribe(this))

  /**
    * The variable total is initially `uninitialized`, that's what the underscore does here. 
    * Total is initialized by calling the compute method.
    */
  private var total: Int = _  // underscore _ means uninitialized
    compute()

  /**
    * The compute method goes through all observed bank accounts, 
    * takes the `current balance` of each and takes the sum of these balances, and stores the result in total.
    */
  private def compute() =
    total = observed.map(_.currentBalance).sum

  /**
    * The handler method of the Subscriber also calls compute. 
    * So whenever one of the bank account changes compute is invoked again to recompute the total balance. 
    * @param publisher the Publisher that invokes the handler method
    */
  override def handler(publisher: Publisher) = compute()

  /**
    * An accessor method to access the total amount of all the bank accounts
    * @return the total amount
    */
  def totalBalance = total
}
