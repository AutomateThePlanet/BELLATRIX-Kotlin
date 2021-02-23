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
package solutions.bellatrix.ios.waitstrategies

import solutions.bellatrix.ios.infrastructure.DriverService.getWrappedIOSDriver
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.ios.configuration.IOSSettings
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.MobileElement
import org.openqa.selenium.WebDriver
import solutions.bellatrix.ios.findstrategies.FindStrategy
import java.lang.Exception
import java.util.function.Function

class ToExistsWaitStrategy : WaitStrategy {
    constructor() {
        timeoutInterval = ConfigurationService.get<IOSSettings>().timeoutSettings.elementToExistTimeout
        sleepInterval = ConfigurationService.get<IOSSettings>().timeoutSettings.sleepInterval
    }

    constructor(timeoutIntervalSeconds: Long, sleepIntervalSeconds: Long) : super(timeoutIntervalSeconds, sleepIntervalSeconds) {}

    override fun <TFindStrategy : FindStrategy> waitUntil(findStrategy: TFindStrategy) {
        val func = Function { w: WebDriver -> elementExists(getWrappedIOSDriver(), findStrategy) }
        waitUntil(func)
    }

    private fun <TFindStrategy : FindStrategy> elementExists(searchContext: IOSDriver<MobileElement>, findStrategy: TFindStrategy): Boolean {
        return try {
            val element = findStrategy.findElement(searchContext)
            element != null
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        fun of(): ToExistsWaitStrategy {
            return ToExistsWaitStrategy()
        }
    }
}