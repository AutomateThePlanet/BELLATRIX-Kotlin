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

package solutions.bellatrix.validations

import org.openqa.selenium.*
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.components.ComponentActionEventArgs
import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import solutions.bellatrix.infrastructure.DriverService
import solutions.bellatrix.plugins.EventListener
import kotlin.NoSuchElementException

class ValidationComponentExtensions {
    private fun waitUntil(waitCondition: () -> Boolean, validationsTimeout:Integer? = null, sleepInterval:Integer? = null) {
        var timeoutInterval = validationsTimeout ?: ConfigurationService.get<WebSettings>().timeoutSettings.validationsTimeout
        var sleepInterval = sleepInterval ?: ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(DriverService.wrappedDriver(), timeoutInterval.toLong(), sleepInterval.toLong())
        webDriverWait.ignoring(NoSuchElementException::class.java, StaleElementReferenceException::class.java)
        try {
            val untilWaitCondition = fun(s: SearchContext) : Boolean = waitCondition()
            webDriverWait.until(untilWaitCondition)
        } catch (ex: WebDriverException) {
            val elementPropertyValidateException = ElementPropertyValidateException(ex.message ?: "", DriverService.wrappedDriver().currentUrl)
            VALIDATED_EXCEPTION_TROWED_EVENT.broadcast(ComponentNotFulfillingValidateConditionEventArgs(ex))
            throw elementPropertyValidateException
        }
    }

    companion object {
        val VALIDATED_EXCEPTION_TROWED_EVENT = EventListener<ComponentNotFulfillingValidateConditionEventArgs>()
    }
}