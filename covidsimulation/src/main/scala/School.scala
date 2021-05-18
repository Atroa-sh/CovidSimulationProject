
case class School(
	override val id: Int,
	override val contagionRate: Double = 1.0, 
	override val width: Int = 30
	) extends Building(id, contagionRate, width) { //placeholder values

		override def toString() = s"School(id: $id)"
}
