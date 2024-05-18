fun elapsedTime(block: () -> Unit): Long {
    val startTime = System.currentTimeMillis()
    block()
    val endTime = System.currentTimeMillis()

    return endTime - startTime
}

fun formatTimeMillis(timeInMillis: Long): String {
    val seconds = timeInMillis.toDouble() / 1000
    return "${String.format("%.3f", seconds)}s"
}

fun tryParseToDouble(value : String) : Double {
    return try {
        val parsedValue = value.replace(oldChar = ',', newChar = '.').toDouble()
//        println("DOUBLE: SUCESSO NO PARSE DO VALOR: ${value} para ${parsedValue}")
        parsedValue
    } catch ( e : NumberFormatException) {
//        println("DOUBLE: ERRO NO PARSE DO VALOR: ${value}. Retorno foi 0.0")
        0.0
    }
}

fun tryParseToInt(value : String) : Int {
    return try {
        val parsedValue = value.replace(oldChar = ',', newChar = '.').toInt()
//        println("INT: SUCESSO NO PARSE DO VALOR: ${value} para ${parsedValue}")
        parsedValue
    } catch ( e : NumberFormatException) {
//        println("INT: ERRO NO PARSE DO VALOR: ${value}. Retorno foi 0")
        0
    }
}