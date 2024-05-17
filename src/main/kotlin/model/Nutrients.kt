package model

data class Nutrients(
     val component: String,
     val unity: String,
     val value: Double,
     val defaultDeviation: String,
     val minValue: Double,
     val maxValue: Double,
     val usedDataValue: Int,
     val references: String,
     val dataType: String,
)
