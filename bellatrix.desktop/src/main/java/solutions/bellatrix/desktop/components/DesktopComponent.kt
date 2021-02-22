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
package solutions.bellatrix.desktop.components

import solutions.bellatrix.desktop.infrastructure.DriverService.getWrappedDriver
import layout.LayoutComponentValidationsBuilder
import io.appium.java_client.windows.WindowsElement
import io.appium.java_client.windows.WindowsDriver
import solutions.bellatrix.desktop.services.AppService
import solutions.bellatrix.desktop.configuration.DesktopSettings
import solutions.bellatrix.core.utilities.InstanceFactory
import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import java.lang.InterruptedException
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.desktop.components.contracts.Component
import solutions.bellatrix.desktop.findstrategies.*
import solutions.bellatrix.desktop.services.ComponentCreateService
import solutions.bellatrix.desktop.services.ComponentWaitService
import solutions.bellatrix.desktop.waitstrategies.ToBeClickableWaitStrategy
import solutions.bellatrix.desktop.waitstrategies.ToBeVisibleWaitStrategy
import solutions.bellatrix.desktop.waitstrategies.ToExistsWaitStrategy
import solutions.bellatrix.desktop.waitstrategies.WaitStrategy
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

open class DesktopComponent : LayoutComponentValidationsBuilder(), Component {
    override lateinit var wrappedElement: WindowsElement
    var parentWrappedElement: WindowsElement? = null
    var elementIndex = 0
    override lateinit var findStrategy: FindStrategy
    val wrappedDriver: WindowsDriver<WindowsElement>
    var appService: AppService
        protected set
    var componentCreateService: ComponentCreateService
        protected set
    var componentWaitService: ComponentWaitService
        protected set
    private val waitStrategies: MutableList<WaitStrategy>
    private val desktopSettings: DesktopSettings
    override val elementName: String
        get() = String.format("%s (%s)", componentClass.simpleName, findStrategy.toString())

    fun waitToBe() {
        findElement()
    }

    fun hover() {
        HOVERING.broadcast(ComponentActionEventArgs(this))
        wrappedDriver.mouse.mouseMove(findElement().coordinates)
        HOVERED.broadcast(ComponentActionEventArgs(this))
    }

    override val componentClass: Class<*>
        get() = javaClass
    override val location: Point
        get() = findElement().location
    override val size: Dimension
        get() = findElement().size

    fun getAttribute(name: String): String {
        return findElement().getAttribute(name)
    }

    fun ensureState(waitStrategy: WaitStrategy) {
        waitStrategies.add(waitStrategy)
    }

    fun <TElementType : DesktopComponent> toExists(): TElementType {
        val waitStrategy = ToExistsWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : DesktopComponent> toBeClickable(): TElementType {
        val waitStrategy = ToBeClickableWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : DesktopComponent> toBeVisible(): TElementType {
        val waitStrategy = ToBeVisibleWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    inline fun <reified TElementType : DesktopComponent, reified TWaitStrategy : WaitStrategy> to(): TElementType {
        val waitStrategy = InstanceFactory.create<TWaitStrategy>()
        this.ensureState(waitStrategy)
        return this as TElementType
    }

    inline fun <reified TComponent : DesktopComponent> createByAccessibilityId(accessibilityId: String): TComponent {
        return create<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : DesktopComponent> createByClass(cclass: String): TComponent {
        return create<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : DesktopComponent> createByXPath(componentClass: Class<TComponent>, xpath: String): TComponent {
        return create<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : DesktopComponent> createByName(name: String): TComponent {
        return create<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : DesktopComponent> createByTag(tag: String): TComponent {
        return create<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : DesktopComponent> createByIdContaining(idContaining: String): TComponent {
        return create<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : DesktopComponent> createByAutomationId(automationId: String): TComponent {
        return create<TComponent, AutomationIdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByAccessibilityId(accessibilityId: String): List<TComponent> {
        return createAll<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByName(name: String): List<TComponent> {
        return createAll<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByClass(cclass: String): List<TComponent> {
        return createAll<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByXPath(xpath: String): List<TComponent> {
        return createAll<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByAutomationId(automationId: String): List<TComponent> {
        return createAll<TComponent, AutomationIdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : DesktopComponent>  createAllByTag(tag: String): List<TComponent> {
        return createAll<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByIdContaining(idContaining: String): List<TComponent> {
        return createAll<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : DesktopComponent, TFindStrategy : FindStrategy> create(value: String): TComponent {
        CREATING_ELEMENT.broadcast(ComponentActionEventArgs(this))
        findElement()
        val component: TComponent = InstanceFactory.create(value)
        component.findStrategy = findStrategy
        component.parentWrappedElement = wrappedElement
        CREATED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return component
    }

    inline fun <reified TComponent : DesktopComponent, reified TFindStrategy : FindStrategy> createAll(value: String): List<TComponent> {
        CREATING_ELEMENTS.broadcast(ComponentActionEventArgs(this))
        findElement()
        val findStrategy = InstanceFactory.create<TFindStrategy>(value)
        val nativeElements = findStrategy.findAllElements(wrappedElement)
        val componentList: MutableList<TComponent> = ArrayList()
        for (i in 0 until nativeElements.stream().count()) {
            val component: TComponent = InstanceFactory.create()
            component.findStrategy = findStrategy
            component.elementIndex = i as Int
            component.parentWrappedElement = wrappedElement
            componentList.add(component)
        }
        CREATED_ELEMENTS.broadcast(ComponentActionEventArgs(this))
        return componentList
    }

    fun findElement(): WindowsElement {
        if (waitStrategies.stream().count() == 0L) {
            waitStrategies.add(toExists())
        }
        try {
            for (waitStrategy in waitStrategies) {
                componentWaitService.wait(this, waitStrategy)
            }
            wrappedElement = findNativeElement()
            scrollToMakeElementVisible(wrappedElement)
            addArtificialDelay()
            waitStrategies.clear()
        } catch (ex: WebDriverException) {
            ex.debugStackTrace()
            print(String.format("\n\nThe element: \n Name: '%s', \n Locator: '%s = %s', \nWas not found on the page or didn't fulfill the specified conditions.\n\n", componentClass.simpleName, findStrategy.toString(), findStrategy.value))
        }
        RETURNING_WRAPPED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return wrappedElement
    }

    protected fun defaultClick(clicking: EventListener<ComponentActionEventArgs>, clicked: EventListener<ComponentActionEventArgs>) {
        clicking.broadcast(ComponentActionEventArgs(this))
        toExists<DesktopComponent>().toBeClickable<DesktopComponent>().waitToBe()
        wrappedElement.click()
        clicked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultGetDisabledAttribute(): Boolean {
        val valueAttr = Optional.ofNullable(getAttribute("disabled")).orElse("false")
        return valueAttr.toLowerCase(Locale.ROOT) === "true"
    }

    protected fun defaultGetText(): String {
        return Optional.ofNullable(findElement().text).orElse("")
    }

    protected fun defaultSetText(settingValue: EventListener<ComponentActionEventArgs>, valueSet: EventListener<ComponentActionEventArgs>, value: String) {
        settingValue.broadcast(ComponentActionEventArgs(this))
        findElement().clear()
        findElement().sendKeys(value)
        valueSet.broadcast(ComponentActionEventArgs(this))
    }

    private fun findNativeElement(): WindowsElement {
        return if (parentWrappedElement == null) {
            findStrategy.findAllElements(wrappedDriver)[elementIndex]
        } else {
            findStrategy.findElement(parentWrappedElement!!) as WindowsElement
        }
    }

    private fun addArtificialDelay() {
        if (desktopSettings.artificialDelayBeforeAction != 0) {
            try {
                Thread.sleep(desktopSettings.artificialDelayBeforeAction.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun scrollToMakeElementVisible(wrappedElement: WindowsElement) {
        // createBy default scroll down to make the element visible.
        if (desktopSettings.automaticallyScrollToVisible) {
            scrollToVisible(wrappedElement, false)
        }
    }

    private fun scrollToVisible(wrappedElement: WindowsElement, shouldWait: Boolean) {
        SCROLLING_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
        try {
            val action = Actions(wrappedDriver)
            action.moveToElement(wrappedElement).perform()
            if (shouldWait) {
                Thread.sleep(500)
                toExists<DesktopComponent>().waitToBe()
            }
        } catch (ex: ElementNotInteractableException) {
            ex.debugStackTrace()
        } catch (ex: InterruptedException) {
            ex.debugStackTrace()
        }
        SCROLLED_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultValidateAttributeSet(supplier: Supplier<String>, attributeName: String) {
        waitUntil({ d: SearchContext? -> !StringUtils.isEmpty(supplier.get()) }, String.format("The control's %s shouldn't be empty but was.", attributeName))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(this, "", String.format("validate %s is empty", attributeName)))
    }

    protected fun defaultValidateAttributeNotSet(supplier: Supplier<String>, attributeName: String) {
        waitUntil({ d: SearchContext? -> StringUtils.isEmpty(supplier.get()) }, String.format("The control's %s should be null but was '%s'.", attributeName, supplier.get()))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(this, "", String.format("validate %s is null", attributeName)))
    }

    protected fun defaultValidateAttributeIs(supplier: Supplier<String>, value: String, attributeName: String) {
        waitUntil({ d: SearchContext? -> supplier.get().strip() == value }, String.format("The control's %s should be '%s' but was '%s'.", attributeName, value, supplier.get()))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(this, value, String.format("validate %s is %s", attributeName, value)))
    }

    protected fun defaultValidateAttributeContains(supplier: Supplier<String>, value: String, attributeName: String) {
        waitUntil({ d: SearchContext? -> supplier.get().strip().contains(value) }, String.format("The control's %s should contain '%s' but was '%s'.", attributeName, value, supplier.get()))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(this, value, String.format("validate %s contains %s", attributeName, value)))
    }

    protected fun defaultValidateAttributeNotContains(supplier: Supplier<String>, value: String, attributeName: String) {
        waitUntil({ d: SearchContext? -> !supplier.get().strip().contains(value) }, String.format("The control's %s shouldn't contain '%s' but was '%s'.", attributeName, value, supplier.get()))
        VALIDATED_ATTRIBUTE.broadcast(ComponentActionEventArgs(this, value, String.format("validate %s doesn't contain %s", attributeName, value)))
    }

    private fun waitUntil(waitCondition: Function<SearchContext, Boolean>, exceptionMessage: String) {
        val webDriverWait = WebDriverWait(getWrappedDriver(), desktopSettings.timeoutSettings.validationsTimeout, desktopSettings.timeoutSettings.sleepInterval)
        try {
            webDriverWait.until(waitCondition)
        } catch (ex: TimeoutException) {
            ex.debugStackTrace()
            throw ex
        }
    }

    companion object {
        val HOVERING = EventListener<ComponentActionEventArgs>()
        val HOVERED = EventListener<ComponentActionEventArgs>()
        val SCROLLING_TO_VISIBLE = EventListener<ComponentActionEventArgs>()
        val SCROLLED_TO_VISIBLE = EventListener<ComponentActionEventArgs>()
        val RETURNING_WRAPPED_ELEMENT = EventListener<ComponentActionEventArgs>()
        val CREATING_ELEMENT = EventListener<ComponentActionEventArgs>()
        val CREATED_ELEMENT = EventListener<ComponentActionEventArgs>()
        val CREATING_ELEMENTS = EventListener<ComponentActionEventArgs>()
        val CREATED_ELEMENTS = EventListener<ComponentActionEventArgs>()
        val VALIDATED_ATTRIBUTE = EventListener<ComponentActionEventArgs>()
    }

    init {
        waitStrategies = ArrayList()
        desktopSettings = ConfigurationService.get<DesktopSettings>()
        appService = AppService
        componentCreateService = ComponentCreateService
        componentWaitService = ComponentWaitService
        wrappedDriver = getWrappedDriver()
    }
}