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
package solutions.bellatrix.desktop.waitstrategies

import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.desktop.findstrategies.FindStrategy
import solutions.bellatrix.desktop.infrastructure.DriverService.getWrappedDriver
import java.util.function.Function

abstract class WaitStrategy {
    var timeoutInterval: Long = 0
        protected set
    var sleepInterval: Long = 0
        protected set

    constructor() {}
    constructor(timeoutInterval: Long, sleepInterval: Long) {
        this.timeoutInterval = timeoutInterval
        this.sleepInterval = sleepInterval
    }

    abstract fun <TFindStrategy : FindStrategy> waitUntil(findStrategy: TFindStrategy)

    fun waitUntil(waitCondition: Function<WebDriver, Boolean>) {
        val webDriverWait = WebDriverWait(getWrappedDriver(), timeoutInterval, sleepInterval)
        webDriverWait.until(waitCondition)
    }

    protected fun findElement(searchContext: SearchContext, by: By): WebElement {
        return searchContext.findElement(by)
    }
}