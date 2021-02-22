package solutions.bellatrix.core.utilities

import java.nio.file.Paths

object UserHomePathNormalizer {
    fun normalizePath(path: String): String {
        return if (path.startsWith("user.home")) {
            Paths.get(userHomePath, path).toString()
        } else path
    }

    val userHomePath: String
        get() = System.getProperty("user.home")
}