import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.select.Elements
import data.Connection.database
import data.FoodsDB
import data.NutrientsDB
import model.Food
import model.Nutrients
import okhttp3.OkHttpClient
import okhttp3.Request
import org.ktorm.dsl.*
import java.io.IOException

fun main() {
    val scraper = Scraper()

    val lastCodeInserted: String? = null; // Use if you paused the script for some reason, put the last nutrients `food_unique_code` inserted on db. For Example: "BRC0007H"
    scraper.runNutrients(lastCodeInserted)
}

