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

fun main() {
    // getFoodsAndInsertOnDatabase()
    val lastCodeInserted: String? = "BRC0262B"; // Use if you paused the script for some reason, put the last nutrients `food_unique_code` inserted on db. For Example: "BRC0007H"
    getNutrientsAndInsertOnDatabase(lastNutrientsFoodCodeInserted = lastCodeInserted)
}

fun fetch(targetUrl: String, param: Param): String? {
    return ApiClient.fetchNutritionalData(targetUrl, param);
}

fun parseRowsToFoods(document: String) : List<Food> {
    val FIELDS_NUMBER = 5
    val doc: Document = Ksoup.parse(document)
    val trs = doc.select(".table tbody tr")
    var result = mutableListOf<Food>()

    for(tr in trs) {
        val dataCols: Elements = tr.select("td a")
        if(dataCols.size === FIELDS_NUMBER) {
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

    val FIELDS_NUMBER = 9
    val doc: Document = Ksoup.parse(document)
    val trs = doc.select("#tabela1 tbody tr")
    var result = mutableListOf<Nutrients>()
    for(tr in trs) {
        val dataCols: Elements = tr.select("td")
        if(dataCols.size === FIELDS_NUMBER) {
            val (component, unity, value, standardDeviation, minValue, maxValue, numberOfData, references, typeOfData) = dataCols
            result.add(Nutrients(component.text(), unity.text(), tryParseToDouble(value.text()), standardDeviation.text(), tryParseToDouble(minValue.text()), tryParseToDouble(maxValue.text()), tryParseToInt(numberOfData.text()), references.text(), typeOfData.text()))
            continue;
        }
        println("ERRO: Quantidade de CAMPOS e NUMERO DE COLUNAS incompatível!")
        break
    }

    return result
}

fun getFoods(): List<Food> {
    val START_PAGE = 1 // Página de inicio da busca
    val TOTAL_PAGES = 57 // Total de Páginas = 57
    val TARGET_URL = "http://www.tbca.net.br/base-dados/composicao_alimentos.php"

    var result: List<Food> = listOf();
    for (page in START_PAGE..TOTAL_PAGES) {
        val tbcaHTML = fetch(TARGET_URL, Param("pagina", page.toString()))
        if(tbcaHTML == null) {
            println("Erro ao buscar dados nutricionais.")
            break
        }
        val foodsData = parseRowsToFoods(tbcaHTML)
        result = result.plus(foodsData)
        println("Salvando dados da página $page...")
    }

    return result
}

fun getNutrients(code: String): List<Nutrients> {
    val TARGET_URL = "https://www.tbca.net.br/base-dados/int_composicao_estatistica.php"
    val nutrientsHTML = fetch(TARGET_URL, Param("cod_produto", code))

    return parseRowsToNutrients(nutrientsHTML!!)
}

fun getFoodsAndInsertOnDatabase() {
    val foodsList = getFoods()

    for( food in foodsList ) {
        database.insert(FoodsDB) {
            set(it.uniqueCode, food.code)
            set(it.portugueseName, food.portugueseName)
            set(it.scientificName, food.scientificName)
            set(it.groupName, food.group)
            set(it.brand, food.brand)
        }
    }
    println("DATABASE: Todos ${foodsList.size} registros inseridos com sucesso.")
}

fun getNutrientsAndInsertOnDatabase(lastNutrientsFoodCodeInserted: String?) { // HardCodedParam
    var query: Query
    var count = 0
    if(lastNutrientsFoodCodeInserted.isNullOrEmpty()) {
        query = database.from(FoodsDB).select(FoodsDB.uniqueCode)
    } else {
        query = database.from(FoodsDB).select(FoodsDB.uniqueCode).where { FoodsDB.uniqueCode greater lastNutrientsFoodCodeInserted}.orderBy(FoodsDB.uniqueCode.asc())
    }

    var totalTimeElapsed: Long = 0

    for ( row in query ) {
        count++

        var nutrientsList: List<Nutrients> = listOf()

        val code = row[FoodsDB.uniqueCode]
        val responseAndParseTime = elapsedTime {
            nutrientsList = getNutrients(code!!)
        }

        if(nutrientsList.isEmpty()) return

        val dbTime = elapsedTime {
            try {
                database.useTransaction {
                    database.batchInsert(NutrientsDB) {
                        for (nutrients in nutrientsList) {
                            this.item {
                                set(it.referencedFoodCode, code)
                                set(it.component, nutrients.component)
                                set(it.unity, nutrients.unity)
                                set(it.value, nutrients.value)
                                set(it.defaultDeviation, nutrients.defaultDeviation)
                                set(it.minValue, nutrients.minValue)
                                set(it.maxValue, nutrients.maxValue)
                                set(it.usedDataValue, nutrients.usedDataValue)
                                set(it.references, nutrients.references)
                                set(it.dataType, nutrients.dataType)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                throw e
            }
        }

        totalTimeElapsed += responseAndParseTime + dbTime
        println("MSG: All nutrients from $code entered successfully! | C: $count | TRI: ${count * 37} | R&PT: ${formatTimeMillis(responseAndParseTime)} | DBIT: ${formatTimeMillis(dbTime)} | Total Time Elapsed: ${formatTimeMillis(totalTimeElapsed)}")
    }
}