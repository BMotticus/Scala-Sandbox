package patterns.ch2.traits

/** 
  * Mixin Compositions adds the possibility of achieving multiple inheritance
  */

/** @Mixing_Traits_In */

// A really simple change and it will also show exactly traits can be mixed in
object MixinRunner extends Ping with Pong {
  def main(args: Array[String]): Unit = {
    ping()
    pong()
  }
}

/** @Composing_Simple_Traits */

class Watch(brand: String, initialTime: Long) {
  def getTime(): Long = System.currentTimeMillis() - initialTime
}

object WatchUser {
  /**
    *  We created two watch instances—one is expensive that has more functionality and is more useful, 
    *  and the other one is a cheap one that does not give too much control.
    */
  def main(args: Array[String]): Unit = {
    // Essentially, they are anonymous classes, which are defined during instantiation.
    val expensiveWatch = new Watch("expensive brand", 1000L) with Alarm with Notifier {
      // We have to implement the abstract methods from the traits we include. 
      override def trigger(): String = "The alarm was triggered."
      override def clear(): Unit = {
        System.out.println("Alarm cleared.")
      }
      override val notificationMessage: String = "Alarm is running!"
    }
    val cheapWatch = new Watch("cheap brand", 1000L) with Alarm {
      override def trigger(): String = "The alarm was triggered."
    }
    // show some watch usage. 
    System.out.println(expensiveWatch.trigger())
    expensiveWatch.printNotification()
    System.out.println(s"The time is ${expensiveWatch.getTime()}.")
    expensiveWatch.clear()

    System.out.println(cheapWatch.trigger())
    System.out.println("Cheap watches cannot manually stop the alarm...")
  }
}

/** @Composing_Complex_Traits */
