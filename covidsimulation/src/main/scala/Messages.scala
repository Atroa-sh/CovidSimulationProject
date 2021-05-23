//import akka.actor.ActorRef

import scala.collection.mutable.ListBuffer


case object GetAge

case class SetWork[T <: Building](work: T)
case object CallToWork
case object LeaveWork

case object InviteFriends
case class FriendInvitation(from: Int)
case class SetFriends(friends: ListBuffer[Citizen[Building]])
case class AddFriend(friend: Citizen[Building])

case object Print

case class Infect(spread: Boolean)
case object Recover

