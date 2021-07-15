package solutions.bellatrix.core.utilities

import java.util.regex.Pattern

object PathNormalizer {
    fun normalizePath(path: String): String {
        var result = path
        result = result.replace("[\\\\/]".toRegex(), System.getProperty("file.separator"))
        val matcher = Pattern.compile("\\$\\{(.*?)}").matcher(result)
        while (matcher.find()) {
            val replacement = resolveVariable(matcher.group(1))
            result = result.replace(matcher.group(0), replacement)
        }
        return result
    }

    fun resolveVariable(variable: String): String {

////    when (variable) {
////        "myVar1" -> return "resolvedVar1";
////        "myVar2" -> return "resolvedVar2";
////        "myVar3" -> return "resolvedVar3";
////    }

        return System.getProperty(variable)
    }
}