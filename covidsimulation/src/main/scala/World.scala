import scala.util.Random
//import akka.actor._
import Gui._
import scala.collection.mutable.ListBuffer

class World(numberOfCitizens: Int, numberOfHomes: Int, numberOfSchools: Int, numberOfWorks: Int) {
  //private val system = ActorSystem()
  val RNG = new Random()
  private val homes: List[Home] = List.tabulate(numberOfHomes)(n => Home(n + 1))
  private val works: List[Work] = List.tabulate(numberOfWorks)(n => Work(n + 1))
  private val schools: List[School] = List.tabulate(numberOfSchools)(n => School(n + 1))
  var peopleReference: List[Citizen[Building]] = generatePeople()
  //val people: List[ActorRef] = generatePeople()


  private def generateWork(age: Int) = 
    if (age < AgeObject.workAge) this.schools(RNG.nextInt(schools.size))
    else this.works(RNG.nextInt(works.size))
  
  private def isWorking(age: Int): Boolean =
    if (RNG.nextDouble() < WorldParameters.unemploymentRate) false
    else if ( //bylo ze osoby w wieku szkolnym nie dostawaly szkoly i przez to graf czasem nie dzialal
      age >= AgeObject.retiredAge
    ) false
    else true

  private def generatePeople(): List[Citizen[Building]] = {
    val people = new ListBuffer[Citizen[Building]]()
    while (people.size < numberOfCitizens) {
      val id = people.size + 1
      val home = homes(RNG.nextInt(homes.size))
      val age = AgeObject.getAge()
      val work = if (isWorking(age)) generateWork(age) else home
      people += new Citizen[Building](id, home, age, work)
      if(isWorking(age)){
        Gui.addCitizen(id.toString,home.id,work.id)
      }
      else{
        Gui.addCitizen(id.toString,home.id,-1)
      }
      home.peopleInside += people(id - 1)
      if (work != home) work.peopleInside += people(id - 1)
    }
    people.toList
  }


  def moveBetweenWork(): Unit = {
    for(i <- peopleReference.indices){
      if(peopleReference(i).jobStart == Params.currentDayTime && isWorking(peopleReference(i).age)){
        peopleReference(i).goToWork()
      }
      else if(peopleReference(i).jobEnd == Params.currentDayTime && isWorking(peopleReference(i).age)){
        peopleReference(i).leaveWork()
      }
    }
  }

  def spreadVirus(): Unit = {
    for(i <- peopleReference.indices){
      if(peopleReference(i).state == StateSIR.Infectious)
        peopleReference(i).spreadInfection()
    }
  }

  def formParties(): Unit = {

  }

  def formFriendships(): Unit = {
    val tmpFriends: List[ListBuffer[Citizen[Building]]] = List.fill(numberOfCitizens)(new ListBuffer[Citizen[Building]])
    val tmpNrOfFriends: List[Int] = List.fill(numberOfCitizens)(RNG.nextInt(3))
    for (i <- 0 until numberOfCitizens) {
      val me: Citizen[Building] = peopleReference(i)
      var added = 0
      while (added < tmpNrOfFriends(i)) {
        val currentIndex = RNG.nextInt(peopleReference.size)
        val current = peopleReference(currentIndex)
        if (!tmpFriends(i).contains(current) && current != me) {
          tmpFriends(i) += current
          tmpFriends(currentIndex) += me
          added += 1
        }
      }
    }
    for (i <- 0 until numberOfCitizens) peopleReference(i).friends = tmpFriends(i)
    //for (i <- 0 until numberOfCitizens) people(i) ! SetFriends(tmpFriends(i))
  }

  def printWorks(): Unit = {
    println("==== Works ====")
    for (w <- works) println(w.toString())
    println("===============")
  }

  def printSchools(): Unit = {
    println("=== Schools ===")
    for (s <- schools) println(s)
    println("===============")
  }

  def printHomes(): Unit = {
    println("==== Homes ====")
    for (h <- homes) println(h)
    println("===============")
  }
  


}
