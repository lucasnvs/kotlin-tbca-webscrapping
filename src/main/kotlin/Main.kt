fun main() {
    val scraper = Scraper()

    val lastCodeInserted: String? = null // Use if you paused the script for some reason, put the last nutrients `food_unique_code` inserted on db. For Example: "BRC0007H"
    scraper.runNutrients(lastCodeInserted)
}

