
import Gui._


object Params{
  val citizens=20
  val homes=5
  val schools=1
  val works=3
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
  Thread.sleep(1000)
  world.people(2) ! InviteFriends
  world.people(2) ! Infect(true)
  Thread.sleep(1000)
  world.shutdown()
}
