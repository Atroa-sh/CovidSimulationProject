object Main extends App {
  val world = new World(20, 10, 1, 3)
  world.formFriendships()
  Thread.sleep(1000) //ustawienie przyjaciul sygnalami chwile zajmuje, inaczej inv do friendow moze dojsc zanim ten ma przyjhacoul
  world.people(2) ! InviteFriends
  world.shutdown()
}
