

case class Work(
    override val contagionRate: Double = 0.3, 
    override val width: Int = 20
    ) extends Building(contagionRate, width) { //placeholder values


}
