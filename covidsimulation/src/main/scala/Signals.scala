import akka.actor.ActorRef

import scala.collection.mutable.ListBuffer

case object GetAge

case class SetWork[T <: Building](work: T)

case object CallToWork

case object InviteFriends

case object FriendInvitation

case class SetFriends(friends: ListBuffer[ActorRef])

case class AddFriend(friend: ActorRef)


