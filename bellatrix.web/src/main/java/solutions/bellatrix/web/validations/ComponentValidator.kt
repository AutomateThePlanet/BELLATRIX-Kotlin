/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Teodor Nikolov
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

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.FluentWait
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.core.utilities.Log
import solutions.bellatrix.web.components.ComponentActionEventArgs
import solutions.bellatrix.web.components.WebComponent
import solutions.bellatrix.web.configuration.WebSettings
import solutions.bellatrix.web.infrastructure.DriverService
import solutions.bellatrix.web.services.BrowserService
import java.time.Duration
import java.util.function.BooleanSupplier
import java.util.function.Supplier

open class ComponentValidator {
    private val timeoutSettings = ConfigurationService.get<WebSettings>().timeoutSettings
    private val browserService = BrowserService

    open fun defaultValidateAttributeIsNull(component: WebComponent, supplier: Supplier<Any?>, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validating ${component.componentName}'s $attributeName is null"))
        waitUntil({ supplier.get() == null }, component, attributeName, "null", supplier, "be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validated ${component.componentName}'s $attributeName is null"))
    }

    open fun defaultValidateAttributeNotNull(component: WebComponent, supplier: Supplier<Any?>, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validating ${component.componentName}'s $attributeName is set"))
        waitUntil({ supplier.get() != null }, component, attributeName, "not null", { "null" }, "not be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validated ${component.componentName}'s $attributeName is set"))
    }

    open fun defaultValidateAttributeIsSet(component: WebComponent, supplier: Supplier<String>, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validating ${component.componentName}'s $attributeName is set"))
        waitUntil({ !StringUtils.isEmpty(supplier.get()) }, component, attributeName, "set", { "not set" }, "be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validated ${component.componentName}'s $attributeName is set"))
    }

    open fun defaultValidateAttributeNotSet(component: WebComponent, supplier: Supplier<String>, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validating ${component.componentName}'s $attributeName is null"))
        waitUntil({ StringUtils.isEmpty(supplier.get()) }, component, attributeName, "not set", supplier, "be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validated ${component.componentName}'s $attributeName is null"))
    }

    open fun defaultValidateAttributeIs(component: WebComponent, supplier: Supplier<String>, value: String, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value, message = "validating ${component.componentName}'s $attributeName is '$value'"))
        waitUntil({ supplier.get().trim() == value }, component, attributeName, value, supplier, "be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value, message = "validated ${component.componentName}'s $attributeName is '$value'"))
    }

    open fun defaultValidateAttributeIs(component: WebComponent, supplier: Supplier<Number>, value: Number, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value.toString(), message = "validating ${component.componentName}'s $attributeName is '$value'"))
        waitUntil({ supplier.get() == value }, component, attributeName, value.toString(), supplier, "be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value.toString(), message = "validated ${component.componentName}'s $attributeName is '$value'"))
    }

    open fun defaultValidateAttributeContains(component: WebComponent, supplier: Supplier<String>, value: String, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value, message = "validating ${component.componentName}'s $attributeName contains '$value'"))
        waitUntil({ supplier.get().trim().contains(value) }, component, attributeName, value, supplier, "contain")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value, message = "validated ${component.componentName}'s $attributeName contains '$value'"))
    }

    open fun defaultValidateAttributeNotContains(component: WebComponent, supplier: Supplier<String>, value: String, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value, message = "validating ${component.componentName}'s $attributeName doesn't contain '$value'"))
        waitUntil({ !supplier.get().trim().contains(value) }, component, attributeName, value, supplier, "not contain")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, value, message = "validated ${component.componentName}'s $attributeName doesn't contain '$value'"))
    }

    open fun defaultValidateAttributeTrue(component: WebComponent, supplier: BooleanSupplier, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validating ${component.componentName} is $attributeName"))
        waitUntil(supplier, component, attributeName, "true", { "false" }, "be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validated ${component.componentName} is $attributeName"))
    }

    open fun defaultValidateAttributeFalse(component: WebComponent, supplier: BooleanSupplier, attributeName: String) {
        VALIDATING_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, message = "validating ${component.componentName} is not $attributeName"))
        waitUntil({ !supplier.asBoolean }, component, attributeName, "false", { "true" }, "be")
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component = component, message = "validated ${component.componentName} is not $attributeName"))
    }

    private fun <T> waitUntil(condition: BooleanSupplier, component: WebComponent, attributeName: String, value: String, supplier: Supplier<T>, prefix: String) {
        val validationTimeout = timeoutSettings.validationsTimeout
        val sleepInterval = timeoutSettings.sleepInterval

        val wait: FluentWait<WebDriver> = FluentWait(DriverService.wrappedDriver())
            .withTimeout(Duration.ofSeconds(validationTimeout))
            .pollingEvery(Duration.ofSeconds(if (sleepInterval > 0) sleepInterval else 1))

        try {
            wait.until {
                component.findElement()
                condition.asBoolean
            }
        } catch (ex: TimeoutException) {
            val error = String.format(
                "\u001B[0mThe %s of \u001B[1m%s \u001B[2m(%s)\u001B[0m%n" +
                        "  Should %s: \"\u001B[1m%s\u001B[0m\"%n" +
                        "  %" + prefix.length + "sBut was: \"\u001B[1m%s\u001B[0m\"%n" +
                        "Test failed on URL: \u001B[1m%s\u001B[0m",
                attributeName, component.componentClass.simpleName, component.findStrategy,
                prefix, value,
                "", supplier.get().toString().replace("%n".toRegex(), "%n" + String.format("%" + (prefix.length + 12) + "s", " ")),
                browserService.url
            )
            Log.error("%n%n%s%n%n", error)
            throw AssertionError(error, ex)
        }
    }

    companion object {
        val VALIDATING_ATTRIBUTE = EventListener<ComponentActionEventArgs>()
        val VALIDATED_ATTRIBUTE = EventListener<ComponentActionEventArgs>()
    }
}