//import akka.actor._
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer


abstract class Building(val id: Int, val contagionRate: Double, val width: Int) {
  val peopleInside: ArrayBuffer[Citizen[Building]] = new ArrayBuffer[Citizen[Building]]()

  def infect(citizenProb: Double, instanceProb: Double): Unit = {
    peopleInside.foreach(p => 
      (if (math.random() < citizenProb * instanceProb) p.reciveInfection()))
  }

  def print(): Unit = {
    println(s"	Building $id")
    for (pearson <- peopleInside) println(pearson)
    Thread.sleep(500)
    println()
  }
}
