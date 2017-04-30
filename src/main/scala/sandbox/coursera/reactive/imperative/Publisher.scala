package sandbox.coursera.reactive.imperative

/**
  * Publisher created by Brandon Mott on 9/25/16.
  */
trait Publisher {
  /**
    * A Publisher is responsible for `internally` maintaining a set of Subscriber.
    */
  private var subscribers: Set[Subscriber] = Set()

  /**
    * Publisher can add a new Subscriber by calling the subscribe method 
    * which simply announces a given Subscriber and adds it to the list of subscribers.
    *
    * @param subscriber a given Subscriber
    */
  def subscribe(subscriber: Subscriber): Unit = subscribers += subscriber

  /**
    * unsubscribe announces a given Subscriber is no longer interested in 
    * `published` info of that Publisher, the implementation of that would simply 
    * remove the given Subscriber from that set of subscribers
    *
    * @param subscriber a given Subscriber
    */
  def unsubscribe(subscriber: Subscriber): Unit = subscribers -= subscriber

  /**
    * The publisher has a publish method that goes through all subscribers and 
    * invokes for each Subscriber a `handler` method that the Subscriber must provide with 
    * the current publisher as it's argument.
    */
  def publish(): Unit = subscribers.foreach(_.handler(this))
}
