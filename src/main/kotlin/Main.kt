import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.select.Elements
import data.Connection.database
import data.FoodsDB
import kotlinx.coroutines.*
import model.Food
import model.Nutrients
import okhttp3.*
import org.ktorm.dsl.from
import org.ktorm.dsl.select
import java.io.IOException
import kotlin.math.max

data class Param(
    val key: String,
    val value: String
)
object ApiClient {

    private val client = OkHttpClient()

    fun fetchNutritionalData(url: String, param: Param): String? {
        val readyUrl = url.plus("?${param.key}=${param.value}")
        val request = Request.Builder()
            .url(readyUrl)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("Code: ${response.code}")
            }
            response.body?.string()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}

fun main() =  runBlocking {
//    val list = getFoods()
//    list.forEach {
//        println(it)
//    }

//    var n = getNutrients("BRC0227B")


}

fun fetch(targetUrl: String, param: Param): String? {
    return ApiClient.fetchNutritionalData(targetUrl, param);
}

fun parseRowsToFoods(document: String) : List<Food> {
    val NUMBER_OF_FIELDS = 5
    val doc: Document = Ksoup.parse(document)
    val trs = doc.select(".table tbody tr")
    var result = mutableListOf<Food>()

    for(tr in trs) {
        val dataCols: Elements = tr.select("td a")
        if(dataCols.size === NUMBER_OF_FIELDS) {
            val (code, name, scientificName, group, brand) = dataCols
            result.add(Food(code.text(), name.text(), scientificName.text(), group.text(), brand.text(), null))
            continue;
        }
        println("ERRO: Quantidade de CAMPOS e NUMERO DE COLUNAS incompatível!")
        break
    }

    return result
}

fun parseRowsToNutrients(document: String) : List<Nutrients> {
    operator fun <T> List<T>.component6(): T = get(5)
    operator fun <T> List<T>.component7(): T = get(6)
    operator fun <T> List<T>.component8(): T = get(7)
    operator fun <T> List<T>.component9(): T = get(8)

    val NUMBER_OF_FIELDS = 9
    val doc: Document = Ksoup.parse(document)
    val trs = doc.select("#tabela1 tbody tr")
    var result = mutableListOf<Nutrients>()
    for(tr in trs) {
        val dataCols: Elements = tr.select("td")
        if(dataCols.size === NUMBER_OF_FIELDS) {
            val (component, unity, value, standardDeviation, minValue, maxValue, numberOfData, references, typeOfData) = dataCols
            println(component)
            result.add(Nutrients(component.text(), unity.text(), value.text(), standardDeviation.text(), minValue.text(), maxValue.text(), numberOfData.text(), references.text(), typeOfData.text()))
            continue;

        }
        println("ERRO: Quantidade de CAMPOS e NUMERO DE COLUNAS incompatível!")
        break
    }

    return result
}

fun getFoods(): List<Food> {
    val TOTAL_PAGES = 2 // 55
    val TARGET_URL = "http://www.tbca.net.br/base-dados/composicao_alimentos.php"

    var result: List<Food> = listOf();
    for (page in 1..TOTAL_PAGES) {
        val tbcaHTML = fetch(TARGET_URL, Param("pagina", page.toString()))
        if(tbcaHTML == null) {
            println("Erro ao buscar dados nutricionais.")
            break
        }
        val foodsData = parseRowsToFoods(tbcaHTML)

        result = result.plus(foodsData)
    }

    return result
}

fun getNutrients(code: String): List<Nutrients> {
    val TARGET_URL = "https://www.tbca.net.br/base-dados/int_composicao_estatistica.php"
    val nutrientsHTML = fetch(TARGET_URL, Param("cod_produto", code))

    return parseRowsToNutrients(nutrientsHTML!!)
}

fun getFoodsAndInsertOnDatabase() {

}
fun getNutrientsAndInsertOnDatabase() {
}