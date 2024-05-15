package model

data class Food (
    private val code: String,
    private val portugueseName: String?,
    private val scientificName: String,
    private val group: String, // maybe id to refer a table on my own database
    private val brand: String, // maybe id to refer a table on my own database
    private val nutrients: List<Nutrients>?
)