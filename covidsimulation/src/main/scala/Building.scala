import akka.actor._
import scala.collection.mutable.ArrayBuffer

abstract class Building(val contagionRate: Double, val width: Int) {
  val peopleInside: ArrayBuffer[ActorRef] = new ArrayBuffer[ActorRef]()

}
