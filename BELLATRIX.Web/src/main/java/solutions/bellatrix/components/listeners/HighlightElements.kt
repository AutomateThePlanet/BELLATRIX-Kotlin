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
package solutions.bellatrix.components.listeners

import solutions.bellatrix.infrastructure.Browser
import java.lang.Runnable
import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import solutions.bellatrix.components.WebComponent
import solutions.bellatrix.components.ComponentActionEventArgs
import solutions.bellatrix.infrastructure.DriverService
import java.lang.Exception

fun WebComponent.highlight() {
    val currentBrowser = DriverService.browserConfiguration().browser
    if (currentBrowser == Browser.CHROME_HEADLESS || currentBrowser == Browser.EDGE_HEADLESS) return
    try {
        val nativeElement = this.wrappedElement
        val originalElementBorder = nativeElement.getCssValue("background-color")
        javaScriptService.execute("arguments[0].style.background='yellow'; return;", nativeElement)
        val runnable = Runnable {
            Thread.sleep(100)
            javaScriptService.execute("arguments[0].style.background='$originalElementBorder'; return;", nativeElement)
        }
        Thread(runnable).start()
    } catch (ignored: Exception) {
    }
}

class HighlightElements {
    companion object {
        private var isHighlightElementsAdded = false
        fun addPlugin() {
            if (!isHighlightElementsAdded) {
                val shouldHighlightElements = ConfigurationService.get<WebSettings>().shouldHighlightElements
                if (shouldHighlightElements) {
                    WebComponent.RETURNING_WRAPPED_ELEMENT.addListener { x: ComponentActionEventArgs -> x.component.highlight() }
                }
                isHighlightElementsAdded = true
            }
        }
    }
}