package solutions.bellatrix.core.utilities

import java.nio.file.Paths

object UserHomePathNormalizer {
    fun normalizePath(path: String): String {
        var result = ""
        if (RuntimeInformation.IS_MAC || RuntimeInformation.IS_UNIX) {
            result = path.replace('\\', '/')
        } else if (RuntimeInformation.IS_WINDOWS) {
            result = path.replace('/', '\\')
        }
        if (path.startsWith("user.home")) {
            result = Paths.get(getUserHomePath(), path.substring(9)).toString()
        }
        return result
    }

    fun getUserHomePath(): String {
        return System.getProperty("user.home")
    }
}