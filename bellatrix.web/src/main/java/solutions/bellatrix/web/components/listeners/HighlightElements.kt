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
package solutions.bellatrix.web.components.listeners

import org.openqa.selenium.NoSuchSessionException
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.Listener
import solutions.bellatrix.web.components.ComponentActionEventArgs
import solutions.bellatrix.web.components.WebComponent
import solutions.bellatrix.web.configuration.WebSettings
import solutions.bellatrix.web.infrastructure.Browser
import solutions.bellatrix.web.infrastructure.DriverService

object HighlightElements : Listener() {
    private var isHighlightElementsAdded = false
    override fun addListener() {
        if (!isHighlightElementsAdded) {
            val shouldHighlightElements = ConfigurationService.get<WebSettings>().shouldHighlightElements
            if (shouldHighlightElements) {
                WebComponent.RETURNING_WRAPPED_ELEMENT.addListener { x: ComponentActionEventArgs -> x.component.highlight() }
            }
            isHighlightElementsAdded = true
        }
    }
}

fun WebComponent.highlight() {
    val currentBrowser = DriverService.browserConfiguration().browser
    if (currentBrowser == Browser.CHROME_HEADLESS) return

    try {
        val originalElementBackground: String = wrappedElement.getCssValue("background")
        val originalElementColor: String = wrappedElement.getCssValue("color")
        val originalElementOutline: String = wrappedElement.getCssValue("outline")
        javaScriptService.execute("arguments[0].style.background='yellow';arguments[0].style.color='black';arguments[0].style.outline='1px solid black';return;", wrappedElement)

        val runnable = Runnable {
            Thread.sleep(100)
            try {
                javaScriptService.execute(
                    "arguments[0].style.background='$originalElementBackground';arguments[0].style.color='$originalElementColor';arguments[0].style.outline='$originalElementOutline';return;",
                    wrappedElement
                )
            } catch (ignored: NoSuchSessionException) {
            } catch (ignored: NullPointerException) {
            }
        }
        Thread(runnable).start()
    } catch (ignored: Exception) {
    }
}
