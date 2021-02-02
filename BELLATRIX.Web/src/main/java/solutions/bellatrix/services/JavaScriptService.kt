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
package solutions.bellatrix.services

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import java.lang.Exception

object JavaScriptService : WebService() {
    private val javascriptExecutor: JavascriptExecutor
        get() = wrappedDriver as JavascriptExecutor

    fun execute(script: String): Any {
        return try {
            javascriptExecutor.executeScript(script) as String
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun execute(frameName: String, script: String): String {
        wrappedDriver.switchTo().frame(frameName)
        val result = execute(script) as String
        wrappedDriver.switchTo().defaultContent()
        return result
    }

    fun execute(script: String, vararg args: Any): String {
        return try {
            javascriptExecutor.executeScript(script, *args) as String
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }

    fun execute(script: String, nativeElement: WebElement): String {
        return try {
            javascriptExecutor.executeScript(script, nativeElement) as String
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }
}