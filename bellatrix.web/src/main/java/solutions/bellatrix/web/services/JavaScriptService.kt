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
package solutions.bellatrix.web.services

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.web.components.WebComponent

object JavaScriptService : WebService() {
    private val javascriptExecutor: JavascriptExecutor
        get() = wrappedDriver as JavascriptExecutor

    fun execute(script: String): Any? {
        return try {
            javascriptExecutor.executeScript(script)
        } catch (ex: Exception) {
            ex.debugStackTrace()
        }
    }

    fun execute(frameName: String, script: String): String? {
        wrappedDriver.switchTo().frame(frameName)
        val result = execute(script)
        wrappedDriver.switchTo().defaultContent()
        if (result != null) {
            return result as String
        } else {
            return null
        }
    }

    fun execute(script: String, vararg args: Any): String? {
        return try {
            val result = javascriptExecutor.executeScript(script, *args)
            if (result != null) result as String else null
        } catch (ex: Exception) {
            ex.debugStackTrace()
            ""
        }
    }

    fun <TComponent : WebComponent> execute(script: String, component: TComponent): String? {
        return execute(script, component.findElement())
    }

    fun execute(script: String, nativeElement: WebElement): String? {
        return try {
            val result = javascriptExecutor.executeScript(script, nativeElement)
            if (result != null) result as String else null
        } catch (ex: Exception) {
            ex.debugStackTrace()
            ""
        }
    }
}