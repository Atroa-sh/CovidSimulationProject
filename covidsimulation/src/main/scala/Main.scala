
import Gui._


object Params{
  val citizens=20
  val homes=5
  val schools=1
  val works=3
  val iterationLag = 50 // in milliseconds
  val dayLength=144
  val lowerWorkBound = 42 // 7 am
  val upperWorkBound = 114 // 7 pm
  var iteration=0
  var currentDayTime = 0
}
object Main extends App {


  Gui.main()
  Gui.addBuildings("H",Params.homes)
  Gui.addBuildings("S",Params.schools)
  Gui.addBuildings("W",Params.works)

  val world = new World(Params.citizens, Params.homes,Params.schools,Params.works)
  world.printWorks()
  world.printSchools()
  world.printHomes()


  // for (p <- world.people) p ! Print
  world.formFriendships()
  // Ze względu na asynchorniczną obsługę wiadomości, czekamy bezpieczną ilość czasu na ich dotarcie
  /*
  world.people(2) ! InviteFriends
  world.people(2) ! Infect(true)
   */


  val thread = new Thread {
    override def run {
      while(Params.iteration<200){ // tymczasowo ograniczony czas
        world.moveBetweenWork()
        world.spreadVirus()


        Params.iteration+=1
        Params.currentDayTime = Params.iteration % Params.dayLength
        Thread.sleep(Params.iterationLag)
      }
    }
  }
  thread.start

}
