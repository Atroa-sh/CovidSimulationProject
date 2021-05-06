case class Home(
	override val contagionRate: Double = 0.1, 
	override val width: Int = 10
	) extends Building(contagionRate, width) { //placeholder values
	

}
