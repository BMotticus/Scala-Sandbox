package sandbox.coursera

/** Imperative Event Handling: The Observer Pattern 
  * `The Observer Pattern` is commonly used when we have some sort of 
  * Model that maintains the state of an application, 
  * and we need to have one or more Views that present the properties of the model in some way.
  *
  * `Variants` of the observer pattern are also called: 
  *   1. publish/subscribe
  *   2. model/view/controller (MVC)
  *
  * Using publish/subscribe:
  *   Subscribe method
  *     `Views` can announce themselves to the `Model` with an operation which we call subscribe.
  *   Publish method
  *     Whenever there's a change, the Model will publish that new information to the Views.
  *   You can then inquire the `Model` about what the new state is and change it's `presentations`. 
  *   There could be more than one `View` that also `subscribes` itself and gets the same `published` information. 
  *
  * Using model/view/controller:
  *   Sometimes in using interfaces, we have a third component which is called a controller, 
  *   which somehow manages the interactions between the model and the `view`.
  */
  
package object reactive {

}
