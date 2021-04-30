object Main extends App {
  val world = new World(5)
  world.formFriendships()
  world.people(1) ! InviteFriends
  world.shutdown()
}
