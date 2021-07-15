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
package solutions.bellatrix.web.waitstrategies

import org.openqa.selenium.*
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.web.configuration.WebSettings

class ToNotExistWaitStrategy : WaitStrategy {
    constructor() : super() {
        timeoutInterval = ConfigurationService.get<WebSettings>().timeoutSettings.elementToExistTimeout
        sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
    }

    constructor(timeoutIntervalSeconds: Long, sleepIntervalSeconds: Long) : super(timeoutIntervalSeconds, sleepIntervalSeconds) {}

    override fun waitUntil(searchContext: SearchContext, by: By) {
        waitUntil { elementExists(searchContext, by) }
    }

    private fun elementExists(searchContext: SearchContext, by: By): Boolean {
        return try {
            val element = findElement(searchContext, by)
            element == null
        } catch (e: NoSuchElementException) {
            true
        } catch (e: TimeoutException) {
            true
        } catch (e: StaleElementReferenceException) {
            true
        }
    }

    companion object {
        fun of(): ToNotExistWaitStrategy {
            return ToNotExistWaitStrategy()
        }
    }
}