import scala.util.Random
import akka.actor._

import scala.collection.mutable.ListBuffer

class World(numberOfCitizens: Int) {
  private val system = ActorSystem()
  final val RNG = new Random()
  val home = new Home()
  val work: Work = new Work()
  val people: List[ActorRef] = List.fill[ActorRef](numberOfCitizens)(system.actorOf(Props(new Citizen(home, work))))

  private def createFriends(numberOfFriends: Int, me: ActorRef): List[ActorRef] = {
    val friends = new ListBuffer[ActorRef]()
    while (friends.size < numberOfFriends) {
      val current = people(RNG.nextInt(people.size))
      if (!friends.contains(current) && current != me) friends += current
    }
    friends.toList
  }

  def shutdown(): Unit = system.terminate()

  def formFriendships(): Unit = {
    for (i <- people) {
      val numberOfFriends: Int = RNG.nextInt(3)
      val friends = createFriends(numberOfFriends, i)
      i ! SetFriends(friends)
    }
  }
}
