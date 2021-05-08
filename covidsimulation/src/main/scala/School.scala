
case class School(
	override val id: Int,
	override val contagionRate: Double = 0.4, 
	override val width: Int = 30
	) extends Building(id, contagionRate, width) { //placeholder values

		
}
