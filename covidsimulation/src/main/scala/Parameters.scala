import scala.math.{pow => pow}

object AgeObject {
    val schoolAge: Int = 6
    val workAge: Int = 20
    val retiredAge: Int = 67

    def getAge(): Int = {
        val x = math.random()
        val age = (
            0.1238043 + 
            74.394384 * x + 
            432.71154 * pow(x, 2) - 
            2391.0028 * pow(x, 3) +
            4952.8016 * pow(x, 4) -
            4332.5399 * pow(x, 5) +
            1079.3973 * pow(x, 6) +
            274.21116 * pow(x, 7)
        ).toInt
        age
    }
}

object WorldParameters {
    // Ewentualnie można tez tu wrzucic liczbe budynków, szkół, obywateli itd

    val unemploymentRate: Double = 0.035
    
    // Tutaj myślę że będą wszystkie parametry modelu, mam na myśli parametry
    // służące odpowiedniej kalibracji taie jak np przepływ powietrza
}