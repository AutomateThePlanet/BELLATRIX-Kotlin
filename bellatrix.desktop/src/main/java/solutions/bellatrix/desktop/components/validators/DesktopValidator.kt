package solutions.bellatrix.desktop.components.validators

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.SearchContext
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.desktop.components.ComponentActionEventArgs
import solutions.bellatrix.desktop.components.DesktopComponent
import solutions.bellatrix.desktop.configuration.DesktopSettings
import solutions.bellatrix.desktop.infrastructure.DriverService
import java.util.function.Function

open class DesktopValidator {
    private val desktopSettings = ConfigurationService.get<DesktopSettings>()

    protected fun defaultValidateAttributeSet(component: DesktopComponent, property: String, attributeName: String) {
        waitUntil({ !StringUtils.isEmpty(property) }, String.format("The control's %s shouldn't be empty but was.", attributeName))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", String.format("validate %s is empty", attributeName)))
    }

    protected fun defaultValidateAttributeNotSet(component: DesktopComponent, property: String, attributeName: String) {
        waitUntil({ StringUtils.isEmpty(property) }, String.format("The control's %s should be null but was '%s'.", attributeName, property))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", String.format("validate %s is null", attributeName)))
    }

    protected fun defaultValidateAttributeIs(component: DesktopComponent, property: String, value: String, attributeName: String) {
        waitUntil({ property.strip() == value }, String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, property))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, String.format("validate %s is %s", attributeName, value)))
    }

    protected fun defaultValidateAttributeContains(component: DesktopComponent, property: String, value: String, attributeName: String) {
        waitUntil({ property.strip().contains(value) }, String.format("The control's %s should contain '%s' but was '%s'.", attributeName, value, property))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, String.format("validate %s contains %s", attributeName, value)))
    }

    protected fun defaultValidateAttributeNotContains(component: DesktopComponent, property: String, value: String, attributeName: String) {
        waitUntil({ !property.strip().contains(value) }, String.format("The control's %s shouldn't contain '%s' but was '%s'.", attributeName, value, property))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, value, String.format("validate %s doesn't contain %s", attributeName, value)))
    }

    protected fun defaultValidateAttributeTrue(component: DesktopComponent, property: Boolean, attributeName: String) {
        waitUntil({ property }, String.format("The control's %s should be null but was '%s'.", attributeName, property))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", String.format("validate is %s", attributeName)))
    }

    protected fun defaultValidateAttributeFalse(component: DesktopComponent, property: Boolean, attributeName: String) {
        waitUntil({ !property }, String.format("The control's %s should be null but was '%s'.", attributeName, property))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(component, "", String.format("validate not %s", attributeName)))
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

    companion object {
        val VALIDATED_ATTRIBUTE = EventListener<ComponentActionEventArgs>()
    }
}