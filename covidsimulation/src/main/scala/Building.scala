import akka.actor._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer


abstract class Building(val id: Int, val contagionRate: Double, val width: Int) {
  val peopleInside: ArrayBuffer[ActorRef] = new ArrayBuffer[ActorRef]()

  def infect(infectiousProb: Double, instanceProb: Double): Unit = {
    if (math.random() < infectiousProb * instanceProb) peopleInside.foreach(p => (p ! Infect(false)))
  }

  def print(): Unit = {
    println(s"	Building $id")
    for (pearson <- peopleInside) pearson ! Print
    Thread.sleep(500)
    println()
  }
}
