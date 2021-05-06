import scala.util.Random
import akka.actor._

import scala.collection.mutable.ListBuffer

class World(numberOfCitizens: Int, numberOfHomes: Int, numberOfSchools: Int, numberOfWorks: Int) {
  private val system = ActorSystem()
  val RNG = new Random()
  private val homes: List[Home] = List.fill(numberOfHomes)(new Home())
  private val works: List[Work] = List.fill(numberOfWorks)(new Work())
  private val schools: List[School] = List.fill(numberOfSchools)(new School())
  val unemploymentRate: Double = 0.035
  val underageRate: Double = 0.15
  val productiveRate: Double = 0.67 + underageRate
  val retiredRate: Double = 1
  val people: List[ActorRef] = generatePeople()


  private def generateWork(age: Int) = 
    if (age < AgeObject.workAge) this.schools(RNG.nextInt(schools.size))
    else this.works(RNG.nextInt(works.size))
  
  private def isWorking(age: Int): Boolean =
    if (RNG.nextDouble() < unemploymentRate) false
    else if (
      age < AgeObject.schoolAge ||
      age >= AgeObject.retiredAge
    ) false
    else true

  private def generatePeople(): List[ActorRef] = {
    val people = new ListBuffer[ActorRef]()
    while (people.size < numberOfCitizens) {
      val home = homes(RNG.nextInt(homes.size))
      val age = AgeObject.getAge()
      val work = if (isWorking(age)) generateWork(age) else home
      people += system.actorOf(Props(new Citizen(home, age, work)))
      println(s"Citizen ${people.size}: age-$age, proffesion-${
        work match {
          case Work(_, _) => "Worker"
          case School(_, _) => "Student"
          case Home(_, _) => "NaN"
        }
      }")
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
