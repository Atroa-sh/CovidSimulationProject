import scala.util.Random
import akka.actor._

import scala.collection.mutable.ListBuffer

class World(numberOfCitizens: Int, numberOfHomes: Int, numberOfSchools: Int, numberOfWorks: Int) {
  private val system = ActorSystem()
  val RNG = new Random()
  private val homes: List[Home] = List.fill(numberOfHomes)(new Home())
  private val works: List[Work] = List.fill(numberOfWorks)(new Work())
  private val schools: List[School] = List.fill(numberOfSchools)(new School())
  val unEmploymentRate: Double = 0.035
  val underageRate: Double = 0.15
  val productiveRate: Double = 0.67 + underageRate
  val retiredRate: Double = 1
  val people: List[ActorRef] = generatePeople()


  private def generateAge(): AgeRange.Value = {
    val roll = RNG.nextDouble()
    if (roll < underageRate) AgeRange.Underage
    else if (roll < productiveRate && roll > underageRate) AgeRange.Productive
    else AgeRange.Retired
  }

  private def generateWork(age: AgeRange.Value) = age match {
    case AgeRange.Underage => this.schools(RNG.nextInt(schools.size))
    case AgeRange.Productive => this.works(RNG.nextInt(works.size))
  }

  private def generatePeople(): List[ActorRef] = {
    val people = new ListBuffer[ActorRef]()
    while (people.size < numberOfCitizens) {
      val home = homes(RNG.nextInt(homes.size))
      val age = generateAge()
      val work = if (age == AgeRange.Retired || RNG.nextDouble() < unEmploymentRate) home else generateWork(age)
      people += system.actorOf(Props(new Citizen(home, age, work)))
    }
    people.toList
  }


  def formFriendships(): Unit = {
    val tmpFriends: List[ListBuffer[ActorRef]] = List.fill(numberOfCitizens)(new ListBuffer[ActorRef])
    val tmpNrOfFriends: List[Int] = List.fill(numberOfCitizens)(RNG.nextInt(3))
    for (i <- 0 until numberOfCitizens) {
      val me: ActorRef = people(i)
      var added = 0
      while (added < tmpNrOfFriends(i)) {
        val currentIndex = RNG.nextInt(people.size)
        val current = people(currentIndex)
        if (!tmpFriends(i).contains(current) && current != me) {
          tmpFriends(i) += current
          tmpFriends(currentIndex) += me
          added += 1
        }
      }
    }
    for (i <- 0 until numberOfCitizens) people(i) ! SetFriends(tmpFriends(i))
  }

  def shutdown(): Unit = system.terminate()
}
