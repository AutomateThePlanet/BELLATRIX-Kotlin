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
package solutions.bellatrix.android.validations

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.SearchContext
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.android.components.AndroidComponent
import solutions.bellatrix.android.components.ComponentActionEventArgs
import solutions.bellatrix.android.configuration.AndroidSettings
import solutions.bellatrix.android.infrastructure.DriverService
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.debugStackTrace
import java.util.function.Function

open class ComponentValidator {
    private val androidSettings = ConfigurationService.get<AndroidSettings>()

    protected open fun defaultValidateAttributeIsNull(component: AndroidComponent, property: Any?, attributeName: String) {
        waitUntil({ property == null }, "The control's $attributeName should be null but was '$property'.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is null"))
    }

    protected open fun defaultValidateAttributeNotNull(component: AndroidComponent, property: Any?, attributeName: String) {
        waitUntil({ property != null }, "The control's $attributeName shouldn't be null but was.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is set"))
    }

    protected open fun defaultValidateAttributeIsSet(component: AndroidComponent, property: String, attributeName: String) {
        waitUntil({ !StringUtils.isEmpty(property) }, "The control's $attributeName shouldn't be empty but was.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is set"))
    }

    protected open fun defaultValidateAttributeNotSet(component: AndroidComponent, property: String, attributeName: String) {
        waitUntil({ StringUtils.isEmpty(property) }, "The control's $attributeName should be null but was '$property'.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is null"))
    }

    protected open fun defaultValidateAttributeIs(component: AndroidComponent, property: String, value: String, attributeName: String) {
        waitUntil({ property.trim() == value }, "The control's $attributeName should be '$value' but was '$property'.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, "validate $attributeName is '$value'"))
    }

    protected open fun defaultValidateAttributeIs(component: AndroidComponent, property: Number?, value: Number, attributeName: String) {
        waitUntil({ property == value }, "The control's $attributeName should be '$value' but was '$property'.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value.toString(), "validate $attributeName is '$value'"))
    }

    protected open fun defaultValidateAttributeContains(component: AndroidComponent, property: String, value: String, attributeName: String) {
        waitUntil({ property.trim().contains(value) }, "The control's $attributeName should contain '$value' but was '$property'.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, "validate $attributeName contains '$value'"))
    }

    protected open fun defaultValidateAttributeNotContains(component: AndroidComponent, property: String, value: String, attributeName: String) {
        waitUntil({ !property.trim().contains(value) }, "The control's $attributeName shouldn't contain '$value' but was '$property'.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, "validate $attributeName doesn't contain '$value'"))
    }

    protected open fun defaultValidateAttributeTrue(component: AndroidComponent, property: Boolean, attributeName: String) {
        waitUntil({ property }, "The control should be '$attributeName' but wasn't.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate is $attributeName"))
    }

    protected open fun defaultValidateAttributeFalse(component: AndroidComponent, property: Boolean, attributeName: String) {
        waitUntil({ !property }, "The control shouldn't be '$attributeName' but was.")
        AndroidComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate not $attributeName"))
    }

    private fun waitUntil(waitCondition: Function<SearchContext, Boolean>, exceptionMessage: String) {
        val webDriverWait = WebDriverWait(DriverService.getWrappedAndroidDriver(), androidSettings.timeoutSettings.validationsTimeout, androidSettings.timeoutSettings.sleepInterval)
        try {
            webDriverWait.until<Boolean>(waitCondition)
        } catch (ex: TimeoutException) {
            ex.debugStackTrace()
            throw ex
        }
    }
}