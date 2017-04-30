
import scala.concurrent._

package object concurrency {

  /** `log` method prints the `current thread` name and a `given string`
    * @param msg - message to print on console */
  def log(msg: String) {
    println(s"${Thread.currentThread.getName}: $msg")
  }

  /** The `thread` method takes a block of code `body`, creates a new Thread that executes this block of code in its `run` method,
    * starts the thread, and returns a reference to the new thread so that the clients can call join on it.
    * @param body - block of code to be executed on new Thread */
  def thread(body: => Unit): Thread = {
    val t = new Thread {
      override def run(): Unit = body
    }
    t.start()
    t
  }

  /** Note: `execute` is a convenience method:
    * To make the code more concise, which executes a block of code on the global ExecutionContext object.
    * @param body - block of code to be executed */
  def execute(body: => Unit) = ExecutionContext.global.execute(
    new Runnable {
      def run() = body
    }
  )
  
}
