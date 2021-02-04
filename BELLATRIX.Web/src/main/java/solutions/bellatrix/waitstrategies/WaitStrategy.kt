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

import org.openqa.selenium.SearchContext
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.WebElement
import solutions.bellatrix.infrastructure.DriverService

abstract class WaitStrategy(protected var timeoutInterval: Long, protected var sleepInterval: Long) {
    constructor() : this(0, 0)

    abstract fun waitUntil(searchContext: SearchContext, by: By)

    protected fun waitUntil(waitCondition: (SearchContext) -> Boolean) {
        val webDriverWait = WebDriverWait(DriverService.wrappedDriver(), timeoutInterval, sleepInterval)
        webDriverWait.until(waitCondition)
    }

    protected fun findElement(searchContext: SearchContext, by: By): WebElement {
        return searchContext.findElement(by)
    }
}