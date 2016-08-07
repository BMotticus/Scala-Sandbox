package patterns.ch2.traits

/** @Traits_As_Interfaces */
//A simple trait
trait Alarm {
  def trigger(): String
}
/**
  * trait Notifier has one implemented method, `printNotification` 
  * one method, `clear`, must be implemented by the mixin class. 
  * the mixin class is required to have one specific variable, `notificationMessage`. Similar to abstract classes in other languages.
  */
trait Notifier {
  //The mixin class definition must have a variable that has 1.) the same name and 2.) be preceded by the "val" keyword.
  val notificationMessage: String
  
  //method already implemented
  def printNotification(): Unit = {
    System.out.println(notificationMessage)
  }
  
  //must be implemented by the mixin class
  def clear()
}

/**
  * One Use Case for implementing required variables is to `pass a variable to the constructor of a class.`  
  * class `NotifierImp` will cover the trait requirements:
  */
class NotifierImpl(val notificationMessage: String) extends Notifier {
  override def clear(): Unit = System.out.println("cleared")
}

/** @Traits_as_classes */
trait Beeper {
  def beep(times: Int): Unit = {
    assert(times >= 0)
    1 to times foreach(i => System.out.println(s"Beep number: $i"))
  }
}

object BeeperRunner extends App{
  val TIMES = 10
  val beeper = new Beeper {}
  beeper.beep(TIMES)
}

/** @Traits_Extending_Classes */
abstract class Connector {
  def connect()
  def close()
}

trait ConnectorWithHelper extends Connector {
  def findDriver(): Unit = {
    System.out.println("Find driver called.")
  }
}

//This class is obliged to implement the abstract class (Connector) methods
class PgSqlConnector extends ConnectorWithHelper {
  override def connect(): Unit = {
    System.out.println("Connected...")
  }

  override def close(): Unit = {
    System.out.println("Closed...")
  }
}

/** @Traits_Extending_Traits */
/**
  * Extending traits is useful in a design pattern called `Stackable Traits`, which we will be looking into later in this book.
  * Traits can also extend each other. Have a look at the following example:
  */
trait Ping {
  def ping(): Unit = {
    System.out.println("ping")
  }
}

trait Pong {
  def pong(): Unit = {
    System.out.println("pong")
  }
}

trait PingPong extends Ping with Pong {
  def pingPong(): Unit = {
    ping()
    pong()
  }
}
//The preceding example is simple and it should really just make the Runner object mix the two traits separately.
object Runner extends PingPong {
  def main(args: Array[String]): Unit = {
    pingPong()
  }
}

