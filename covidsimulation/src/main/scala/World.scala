import scala.util.Random
import akka.actor._

import scala.collection.mutable.ListBuffer

class World(numberOfCitizens: Int, numberOfHomes: Int, numberOfSchools: Int, numberOfWorks: Int) {
  private val system = ActorSystem()
  val RNG = new Random()
  private val homes: List[Home] = List.tabulate(numberOfHomes)(n => new Home(n + 1))
  private val works: List[Work] = List.tabulate(numberOfWorks)(n => new Work(n + 1))
  private val schools: List[School] = List.tabulate(numberOfSchools)(n => new School(n + 1))
  val people: List[ActorRef] = generatePeople()


  private def generateWork(age: Int) = 
    if (age < AgeObject.workAge) this.schools(RNG.nextInt(schools.size))
    else this.works(RNG.nextInt(works.size))
  
  private def isWorking(age: Int): Boolean =
    if (RNG.nextDouble() < WorldParameters.unemploymentRate) false
    else if (
      age < AgeObject.schoolAge ||
      age >= AgeObject.retiredAge
    ) false
    else true

  private def generatePeople(): List[ActorRef] = {
    val people = new ListBuffer[ActorRef]()
    while (people.size < numberOfCitizens) {
      val id = people.size + 1;
      val home = homes(RNG.nextInt(homes.size))
      val age = AgeObject.getAge()
      val work = if (isWorking(age)) generateWork(age) else home
      people += system.actorOf(Props(new Citizen(id, home, age, work)))
      home.peopleInside += people(id - 1);
      if (work != home) work.peopleInside += people(id - 1)
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

  def printWorks(): Unit = {
    println("==== Works ====")
    for (w <- works) w.print()
    println("===============")
  }

  def printSchools(): Unit = {
    println("=== Schools ===")
    for (s <- schools) s.print()
    println("===============")
  }

  def printHomes(): Unit = {
    println("==== Homes ====")
    for (h <- homes) h.print()
    println("===============")
  }
  

  def shutdown(): Unit = system.terminate()
}
