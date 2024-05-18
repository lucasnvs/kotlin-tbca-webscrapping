enum class Color {
    RED, GREEN, YELLOW, BLUE, CYAN, WHITE
}

object Logger {
    fun info(tag: String, message: String) {
        val build = "[INFO] Tag: $tag > $message"
        println(build)
    }

    fun success(tag: String, message: String) {
        val build = "[SUCCESS] Tag: $tag > $message"
        println(paint(build, Color.GREEN))
    }

    fun warning(tag: String, message: String) {
        val build = "[WARNING] Tag: $tag > $message"
        println(paint(build, Color.RED))
    }
}

fun paint(txt : String, color: Color): String {
    when(color) {
        Color.RED -> return "\u001B[31m$txt\u001B[0m"
        Color.GREEN -> return "\u001B[32m$txt\u001B[0m"
        Color.YELLOW -> return "\u001B[33m$txt\u001B[0m"
        Color.BLUE -> return "\u001B[34m$txt\u001B[0m"
        Color.CYAN -> return "\u001B[36m$txt\u001B[0m"
        Color.WHITE -> return "\u001B[37m$txt\u001B[0m"
    }
}
