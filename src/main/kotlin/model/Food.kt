package model

data class Food (
    val code: String,
    val portugueseName: String?,
    val scientificName: String,
    val group: String, // maybe id to refer a table on my own database
    val brand: String, // maybe id to refer a table on my own database
    val nutrients: List<Nutrients>?
)