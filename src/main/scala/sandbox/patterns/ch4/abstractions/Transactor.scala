package sandbox.patterns.ch4.abstractions

import scala.collection._
/** `Transactor` is used to persist information into a database
  * The `transact` method will do some transformations on the data and then insert it in our database
  * 
  * `this: Database[T] =>` is the `Self Type`, 
  * This allows us to access the methods of our included types directly as if they were methods of the trait that includes them. 
  * In this case, `Database`, which gives us the `save` method.
  * Another way of doing the same here is by writing `self: Database[T] =>` instead. */
trait Transactor[T] { this: Database[T] => 
  def transact(data: T): Unit = {
    println("Calling transact.")
    save(data)
  }
}

/** We have a base trait `Database` and then some concrete database implementations `MemoryDatabase` & `FileDatabase`
  * To have well written code the database implementations will be separate from the base trait. */
trait Database[T] {
  def save(data: T)
}

trait MemoryDatabase[T] extends Database[T] {
  val db: mutable.MutableList[T] = mutable.MutableList.empty

  override def save(data: T): Unit = {
    println("Saving to in memory database.")
    db.+=:(data)
  }
}

trait FileDatabase[T] extends Database[T] {
  override def save(data: T): Unit = {
    println("Saving to file.")
  }
}

class FileTransactor[T] extends Transactor[T] with FileDatabase[T]

class MemoryTransactor[T] extends Transactor[T] with MemoryDatabase[T]

/** `History` is a component that could potentially keep track of changes to roll back at some point */
trait History {
  def add(): Unit = {
    println("Action added to history.")
  }
}

/** Using multiple components:
  * Here we use the `History` component and the `Database` in our `Persister` trait.
  * Using the `with` keyword, we can add as many requirements as we like. */
trait Persister[T] {
  this: Database[T] with History =>
  def persist(data: T): Unit = {
    System.out.println("Calling persist.")
    save(data)
    add()
  }
}


/**
  * The reason for this is that we now must mix `History` in every class that uses `Persister`
  */
class FilePersister[T] extends Persister[T] with FileDatabase[T] with History

class MemoryPersister[T] extends Persister[T] with MemoryDatabase[T] with History

