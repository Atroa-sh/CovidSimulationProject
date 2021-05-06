
case class School(
    override val contagionRate: Double = 0.4, 
    override val width: Int = 30
    ) extends Building(contagionRate, width) { //placeholder values


}
