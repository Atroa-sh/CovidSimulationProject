import akka.actor._


class Citizen(val home: Home, val work: Work) extends Actor {
  var wearsMask: Boolean = false
  var busy: Boolean = false
  var friends: List[ActorRef] = List()

  def initializeFriends(friends: List[ActorRef]) = {
    this.friends = friends
  }

  def goToWork: Unit = {

  }

  def inviteFriend() = {
    for (i <- friends) i ! FriendInvitation
  }

  def receive = {
    case CallToWork => goToWork
    case InviteFriends => if(friends.size<1) println("[" + self.path.name + "]:" + " I dont have any friends FeelsBadMan") else inviteFriend()
    case FriendInvitation => println("[" + self.path.name + "]:" + " I got inv from " + "[" + sender.path.name + "]")
    case SetFriends(friends) => this.friends = friends
  }
}

