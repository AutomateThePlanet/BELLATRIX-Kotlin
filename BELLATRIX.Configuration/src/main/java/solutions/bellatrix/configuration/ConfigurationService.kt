/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package solutions.bellatrix.configuration

import java.util.Properties
import java.io.IOException
import com.google.gson.JsonParser
import com.google.gson.Gson
import org.apache.commons.io.IOUtils
import java.lang.Exception
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets

object ConfigurationService {
    inline fun <reified TConfigSection> get(): TConfigSection {
        var environment: String? = null
        var mappedObject: TConfigSection = TConfigSection::class.java.getDeclaredConstructor().newInstance() as TConfigSection
        if (environment == null) {
            val environmentOverride = System.getProperty("environment")
            if (environmentOverride == null) {
                val input = ConfigurationService::class.java.getResourceAsStream("/application.properties")
                val p = Properties()
                try {
                    p.load(input)
                } catch (e: IOException) {
                    return mappedObject
                }
                environment = p.getProperty("environment")
            } else {
                environment = environmentOverride
            }
        }
        val fileName = String.format("testFrameworkSettings.%s.json", environment)
        val jsonFileContent = getFileAsString(fileName)
        val sectionName = getSectionName(TConfigSection::class.java)
        val jsonObject = JsonParser.parseString(jsonFileContent).asJsonObject[sectionName].toString()
        val gson = Gson()
        try {
            mappedObject = gson.fromJson(jsonObject, TConfigSection::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mappedObject
    }

    fun getSectionName(configSection: Class<*>): String {
        val sb = StringBuilder(configSection.simpleName)
        sb.setCharAt(0, Character.toLowerCase(sb[0]))
        return sb.toString()
    }

    fun getFileAsString(fileName: String): String? {
        return try {
            val input = ConfigurationService::class.java.getResourceAsStream("/$fileName")
            IOUtils.toString(input, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            null
        }
    }
}