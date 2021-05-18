import akka.actor._

import scala.collection.mutable.ListBuffer
import scala.util.Random


object StateSIR extends Enumeration {
  type StateSIR = Value
  val Suspectible, Infectious, Recovered = Value
}

import StateSIR._

class Citizen[T <: Building](
  val id: Int, val home: Home, val age: Int, val work: T
  ) extends Actor with ActorLogging {
  val proffesion: String = work match {
    case Work(_, _, _) => "Worker"
    case School(_, _, _) => "Student"
    case _ => "NaN"
  }
  val RNG = new Random()
  var wearsMask: Boolean = false
  var busy: Boolean = false
  var friends: ListBuffer[ActorRef] = new ListBuffer[ActorRef]()
  var state: StateSIR = Suspectible
  val infectionRate = 0.5

  def receive = {
    case CallToWork => goToWork

    case SetFriends(friends) => initializeFriends(friends)
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

  def reciveInfection(): Unit = {
    if (state == Suspectible) {
      state = Infectious
      println(s"($id) Got Infected!")
    }
  }

  def spreadInfection(): Unit = {
    work.infect(infectionRate, work.contagionRate)
    state = Infectious
    println(s"(Cit. $id) spreading virus in " + work.toString())
  }

  def initializeFriends(friends: ListBuffer[ActorRef]):Unit = {
    this.friends = friends
  }

  def goToWork: Unit = {

  }

  def inviteFriends(): Unit = {
    for (i <- friends) i ! FriendInvitation(id)
  }


  override def toString(): String = s"Citizen $id, age: $age, proffesion: $proffesion"
}

