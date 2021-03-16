package solutions.bellatrix.desktop.validations

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.SearchContext
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.desktop.components.ComponentActionEventArgs
import solutions.bellatrix.desktop.components.DesktopComponent
import solutions.bellatrix.desktop.configuration.DesktopSettings
import solutions.bellatrix.desktop.infrastructure.DriverService
import java.util.function.Function

open class ComponentValidator {
    private val desktopSettings = ConfigurationService.get<DesktopSettings>()

    protected open fun defaultValidateAttributeIsNull(component: DesktopComponent, property: Any?, attributeName: String) {
        waitUntil({ property == null }, "The control's $attributeName should be null but was '$property'.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is null"))
    }

    protected open fun defaultValidateAttributeNotNull(component: DesktopComponent, property: Any?, attributeName: String) {
        waitUntil({ property != null }, "The control's $attributeName shouldn't be null but was.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is set"))
    }

    protected open fun defaultValidateAttributeIsSet(component: DesktopComponent, property: String, attributeName: String) {
        waitUntil({ !StringUtils.isEmpty(property) }, "The control's $attributeName shouldn't be empty but was.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is set"))
    }

    protected open fun defaultValidateAttributeNotSet(component: DesktopComponent, property: String, attributeName: String) {
        waitUntil({ StringUtils.isEmpty(property) }, "The control's $attributeName should be null but was '$property'.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate $attributeName is null"))
    }

    protected open fun defaultValidateAttributeIs(component: DesktopComponent, property: String, value: String, attributeName: String) {
        waitUntil({ property.trim() == value }, "The control's $attributeName should be '$value' but was '$property'.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, "validate $attributeName is '$value'"))
    }

    protected open fun defaultValidateAttributeIs(component: DesktopComponent, property: Number?, value: Number, attributeName: String) {
        waitUntil({ property == value }, "The control's $attributeName should be '$value' but was '$property'.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value.toString(), "validate $attributeName is '$value'"))
    }

    protected open fun defaultValidateAttributeContains(component: DesktopComponent, property: String, value: String, attributeName: String) {
        waitUntil({ property.trim().contains(value) }, "The control's $attributeName should contain '$value' but was '$property'.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, "validate $attributeName contains '$value'"))
    }

    protected open fun defaultValidateAttributeNotContains(component: DesktopComponent, property: String, value: String, attributeName: String) {
        waitUntil({ !property.trim().contains(value) }, "The control's $attributeName shouldn't contain '$value' but was '$property'.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, "validate $attributeName doesn't contain '$value'"))
    }

    protected open fun defaultValidateAttributeTrue(component: DesktopComponent, property: Boolean, attributeName: String) {
        waitUntil({ property }, "The control should be '$attributeName' but wasn't.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate is $attributeName"))
    }

    protected open fun defaultValidateAttributeFalse(component: DesktopComponent, property: Boolean, attributeName: String) {
        waitUntil({ !property }, "The control shouldn't be '$attributeName' but was.")
        DesktopComponent.VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", "validate not $attributeName"))
    }

    private fun waitUntil(waitCondition: Function<SearchContext, Boolean>, exceptionMessage: String) {
        val webDriverWait = WebDriverWait(DriverService.getWrappedDriver(), desktopSettings.timeoutSettings.validationsTimeout, desktopSettings.timeoutSettings.sleepInterval)
        try {
            webDriverWait.until(waitCondition)
        } catch (ex: TimeoutException) {
            ex.debugStackTrace()
            throw ex
        }
    }
}