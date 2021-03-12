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

package solutions.bellatrix.web.validations

import org.openqa.selenium.SearchContext
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.web.components.contracts.Component
import solutions.bellatrix.web.components.contracts.ComponentHref
import solutions.bellatrix.web.components.contracts.ComponentHtml
import solutions.bellatrix.web.configuration.WebSettings
import solutions.bellatrix.web.infrastructure.DriverService

fun <TComponent : ComponentHref> TComponent.validateHrefIs(expected: String, validationsTimeout: Int? = null, sleepInterval: Int? = null) {
    Validator.waitUntil({ this.href.trim() == expected },
            this,
            "validate ${this.elementName} href is '$expected'",
            "The component's href should be '$expected' but was '$this.href'.",
            expected,
            validationsTimeout,
            sleepInterval)
}

fun <TComponent : ComponentHref> TComponent.validateHrefIsSet(validationsTimeout: Int? = null, sleepInterval: Int? = null) {
    Validator.waitUntil({ !this.href.trim().isBlank() },
            this,
            "validate ${this.elementName} href is set",
            "The component's href shouldn't be empty but was.",
            null,
            validationsTimeout,
            sleepInterval)
}

fun <TComponent : ComponentHtml> TComponent.validateHtmlIs(expected: String, validationsTimeout: Int? = null, sleepInterval: Int? = null) {
    Validator.waitUntil({ this.html == expected },
            this,
            "validate ${this.elementName} HTML is '$expected'",
            "The component's HTML should be '$expected' but was '${this.html}'.",
            expected,
            validationsTimeout,
            sleepInterval)
}

fun <TComponent : ComponentHtml> TComponent.validateHtmlContains(expected: String, validationsTimeout: Int? = null, sleepInterval: Int? = null) {
    Validator.waitUntil({ this.html.contains(expected) },
            this,
            "validate ${this.elementName} HTML contains '$expected'",
            "The component's HTML should contain '$expected' but was '${this.html}'.",
            expected,
            validationsTimeout,
            sleepInterval)
}

object Validator {
    val VALIDATED_EXCEPTION_TROWED_EVENT = EventListener<ComponentNotFulfillingValidateConditionEventArgs>()
    val VALIDATED_EVENT = EventListener<ValidationEventArgs>()

    fun <TComponent : Component> waitUntil(
            waitCondition: () -> Boolean,
            component: TComponent,
            successMessage: String,
            exceptionMessage: String,
            actionValue: String? = null,
            validationsTimeout: Int? = null,
            sleepInterval: Int? = null) {
        val currentTimeoutInterval = validationsTimeout
                ?: ConfigurationService.get<WebSettings>().timeoutSettings.validationsTimeout
        val currentSleepInterval = sleepInterval
                ?: ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(DriverService.wrappedDriver(), currentTimeoutInterval.toLong(), currentSleepInterval.toLong())
        webDriverWait.ignoring(NoSuchElementException::class.java, StaleElementReferenceException::class.java)
        try {
            val untilWaitCondition = fun(s: SearchContext): Boolean = waitCondition()
            webDriverWait.until(untilWaitCondition)
            VALIDATED_EVENT.broadcast(ValidationEventArgs(component, successMessage, actionValue))
        } catch (ex: WebDriverException) {
            val elementPropertyValidateException = ElementPropertyValidateException(exceptionMessage, DriverService.wrappedDriver().currentUrl)
            VALIDATED_EXCEPTION_TROWED_EVENT.broadcast(ComponentNotFulfillingValidateConditionEventArgs(ex))
            throw elementPropertyValidateException
        }
    }
}