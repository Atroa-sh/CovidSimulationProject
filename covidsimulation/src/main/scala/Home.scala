
case class Home(
	override val id: Int,
	override val contagionRate: Double = 0.1, 
	override val width: Int = 10
	) extends Building(id, contagionRate, width) { //placeholder values
	
		
}
