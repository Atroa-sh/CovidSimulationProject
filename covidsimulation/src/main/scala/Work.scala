
case class Work(
	override val id: Int,
	override val contagionRate: Double = 0.3, 
	override val width: Int = 20
	) extends Building(id, contagionRate, width) { //placeholder values

		
}
