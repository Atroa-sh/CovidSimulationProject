object Main extends App {
  val world = new World(20, 5, 1, 3)
  world.printWorks()
  world.printSchools()
  world.printHomes()
  // for (p <- world.people) p ! Print
  world.formFriendships()
  // Ze względu na asynchorniczną obsługę wiadomości, czekamy bezpieczną ilość czasu na ich dotarcie
  Thread.sleep(1000)
  world.people(2) ! InviteFriends
  world.shutdown()
}
