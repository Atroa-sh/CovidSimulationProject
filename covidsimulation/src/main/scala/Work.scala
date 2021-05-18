
case class Work(
	override val id: Int,
	override val contagionRate: Double = 1.0, 
	override val width: Int = 20
	) extends Building(id, contagionRate, width) { //placeholder values

		override def toString() = s"Work(id: $id)"
}
