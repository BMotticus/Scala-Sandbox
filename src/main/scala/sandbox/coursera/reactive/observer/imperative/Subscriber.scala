package sandbox.coursera.reactive.observer.imperative

/**
  * Subscriber created by Brandon Mott on 9/25/16.
  */
trait Subscriber {
  /**
    * Each [[Subscriber]] needs to have is a [[handler]] method, 
    * The [[Publisher]] that published new information as a parameter to that handler.
    *
    * @param publisher the [[Publisher]] that invokes the handler method  
    */
  def handler(publisher: Publisher)
}
