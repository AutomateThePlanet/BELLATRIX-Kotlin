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
package solutions.bellatrix.waitstrategies

import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import org.openqa.selenium.SearchContext
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException

class ToBeVisibleWaitStrategy : WaitStrategy {
    constructor() : super() {
        timeoutInterval = ConfigurationService.get<WebSettings>().timeoutSettings.elementToBeVisibleTimeout
        sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
    }

    constructor(timeoutIntervalSeconds: Long, sleepIntervalSeconds: Long) : super(timeoutIntervalSeconds, sleepIntervalSeconds) {}

    override fun waitUntil(searchContext: SearchContext, by: By) {
        waitUntil { elementIsVisible(searchContext, by) }
    }

    private fun elementIsVisible(searchContext: SearchContext, by: By): Boolean {
        val element = findElement(searchContext, by)
        return try {
            element != null && element.isDisplayed
        } catch (e: StaleElementReferenceException) {
            false
        } catch (e: NoSuchElementException) {
            false
        }
    }

    companion object {
        fun of(): ToBeVisibleWaitStrategy {
            return ToBeVisibleWaitStrategy()
        }
    }
}