import akka.actor.ActorRef

case object CallToWork

case object InviteFriends

case object FriendInvitation

case class SetFriends(friends: List[ActorRef])


