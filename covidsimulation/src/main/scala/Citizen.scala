//import akka.actor._
import Gui._
import scala.collection.mutable.ListBuffer
import scala.util.Random


object StateSIR extends Enumeration {
  type StateSIR = Value
  val Suspectible, Infectious, Recovered = Value
}

import StateSIR._

class Citizen[T <: Building](
  val id: Int, val home: Home, val age: Int, val work: T
  ){
  val proffesion: String = work match {
    case Work(_, _, _) => "Worker"
    case School(_, _, _) => "Student"
    case _ => "NaN"
  }
  val RNG = new Random()
  val shortestWork = 5
  val longestWork = 10
  val jobLength: Int =  if(this.work==this.home || age >= AgeObject.retiredAge) 0 else shortestWork + RNG.nextInt((longestWork - shortestWork) + 1)
  val jobStart: Int = if(jobLength>0) Params.lowerWorkBound + RNG.nextInt((Params.upperWorkBound - (Params.lowerWorkBound + jobLength)) + 1)else -1
  val jobEnd: Int = if(jobLength>0) jobStart + jobLength else -1
  //val partyFactor: Double = 0.005 * RNG.nextInt(6) // moze cos takiego do zapraszania ludzi?
  var wearsMask: Boolean = false
  var busy: Boolean = false
  var friends: ListBuffer[Citizen[Building]] = new ListBuffer[Citizen[Building]]()
  var state: StateSIR = Suspectible
  val infectionRate = 0.5
  /*
  def receive = {
    case CallToWork => goToWork
    case LeaveWork => leaveWork

    //case SetFriends(friends) => initializeFriends(friends)
    case InviteFriends => 
      if (friends.size < 1) log.info(s"\n[$id]: I don't have any friends :(") 
      else inviteFriends()
    case FriendInvitation(inviter) => log.info(s"\n[$id]: Recieved invitation from [$inviter]")

    case Infect(spread) => if (spread) spreadInfection() else reciveInfection()
    case Recover => state = Recovered   // Baaardzo wstępnie

    case Print => println(this)

    //case SetWork(work) => this.work = work
    //case GetAge => this.age

  }
*/

  def reciveInfection(): Unit = {
    if (state == Suspectible) {
      state = Infectious
      Gui.infect(id.toString)
      println(s"($id) Got Infected!")
    }
  }

  def spreadInfection(): Unit = {
    work.infect(infectionRate, work.contagionRate)
    state = Infectious
    println(s"(Cit. $id) spreading virus in " + work.toString())
  }

  def initializeFriends(friends: ListBuffer[Citizen[Building]]):Unit = {
    this.friends = friends
  }

  def goToWork(): Unit = {
    println("Going to work")
  }

  def leaveWork(): Unit = {
    println("Leaving work")
  }

  def inviteFriends(): Unit = {
    for (i <- friends) i.receiveInvitation()
  }

  def receiveInvitation(): Unit = {
    println("I received invitation")
  }


  override def toString(): String = s"Citizen $id, age: $age, proffesion: $proffesion id: $id"
}

