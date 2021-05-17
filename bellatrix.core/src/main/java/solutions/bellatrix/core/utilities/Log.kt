package solutions.bellatrix.core.utilities

object Log {
    fun info(message: String, vararg args: Any?) {
        System.out.printf("$message%n", *args)
    }

    fun error(message: String, vararg args: Any?) {
        System.err.printf("$message%n", *args)
    }
}