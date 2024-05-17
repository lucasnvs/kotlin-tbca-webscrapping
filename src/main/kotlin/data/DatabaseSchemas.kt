package data

import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object FoodsDB : Table<Nothing>("foods") {
    val id = int("id").primaryKey()
    val uniqueCode = varchar("food_unique_code")
    val portugueseName = varchar("portuguese_name")
    val scientificName = varchar("scientific_name")
    val groupName = varchar("group_name")
    val brand = varchar("brand")
}

object NutrientsDB : Table<Nothing>("nutrients") {
    val id = int("id").primaryKey()
    val referencedFoodCode = varchar("food_unique_code")
    val component = varchar("n_component")
    val unity = varchar("unity")
    val value = double("value_by_hundred_grams")
    val defaultDeviation = varchar("default_deviation")
    val minValue = double("min_value")
    val maxValue = double("max_value")
    val usedDataValue = int("used_data_value")
    val references = varchar("n_references")
    val dataType = varchar("data_type")
}