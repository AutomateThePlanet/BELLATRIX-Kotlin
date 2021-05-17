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

import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.Listener
import solutions.bellatrix.web.components.ComponentActionEventArgs
import solutions.bellatrix.web.components.WebComponent
import solutions.bellatrix.web.configuration.WebSettings
import solutions.bellatrix.web.infrastructure.Browser
import solutions.bellatrix.web.infrastructure.DriverService
import solutions.bellatrix.web.services.JavaScriptService

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
    if (currentBrowser == Browser.CHROME_HEADLESS || currentBrowser == Browser.EDGE_HEADLESS) return
    try {
        val originalElementBorder = wrappedElement.getCssValue("background-color") ?: ""
        JavaScriptService.execute("arguments[0].style.background='yellow'; return;", wrappedElement)
        val runnable = Runnable {
            Thread.sleep(100)
            JavaScriptService.execute("arguments[0].style.background='$originalElementBorder'; return;", wrappedElement)
        }
        Thread(runnable).start()
    } catch (ignored: Exception) {
    }
}
