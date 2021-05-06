import akka.actor._

import scala.collection.mutable.ListBuffer
import scala.util.Random

object AgeRange extends Enumeration {
  type Age = Value
  val Underage, Productive, Retired = Value
}

class Citizen[T <: Building](val home: Home, val age: Int, val work: T) extends Actor {
  val RNG = new Random()
  var wearsMask: Boolean = false
  var busy: Boolean = false
  var friends: ListBuffer[ActorRef] = new ListBuffer[ActorRef]()
  var dead: Boolean = false


  def initializeFriends(friends: ListBuffer[ActorRef]):Unit = {
    this.friends = friends
  }

  def goToWork: Unit = {

  }

  def inviteFriend():Unit = {
    for (i <- friends) i ! FriendInvitation
  }


  def receive = {
    case CallToWork => goToWork
    case InviteFriends => if (friends.size < 1) println("[" + self.path.name + "]:" + " I don't have any friends FeelsBadMan") else inviteFriend()
    case FriendInvitation => println("[" + self.path.name + "]:" + " I got inv from " + "[" + sender.path.name + "]")
    case SetFriends(friends) => initializeFriends(friends)

    //case SetWork(work) => this.work = work
    //case GetAge => this.age

  }
}

