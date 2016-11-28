package com.brandonmott.patterns.ch10.functional

/**
  * A functor is a class that `has a map method` and `conforms to functor laws`.
  * <b>The Functor laws:</b>
  * <br>Identity: Whenever the identity function is mapped over some data, it doesn't change it. 
  *       In other words, map(x)(i => i) == x.
  *   
  * <br>Composition: Multiple maps must compose together. 
  *       It should make no difference if we do this operation: 
  *       x.map(i => y(i)).map(i => z(i)) or x.map(i => z(y(i))). 
  *   
  * <br>The `map` Function: The map function preserves the structure of the data, for example, 
  *       it does not add or remove elements, change their order, and so on. It just changes the representation.
 */
package object functors {
  
}
