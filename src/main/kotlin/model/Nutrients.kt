package model

data class Nutrients(
    private val component: String,
    private val unity: String,
    private val value: String,
    private val defaultDeviation: String,
    private val minValue: String,
    private val maxValue: String,
    private val usedDataValue: String,
    private val references: String,
    private val dataType: String,
)
