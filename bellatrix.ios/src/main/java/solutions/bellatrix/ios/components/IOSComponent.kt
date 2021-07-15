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
package solutions.bellatrix.ios.components

import io.appium.java_client.MobileElement
import io.appium.java_client.ios.IOSDriver
import layout.LayoutComponentValidationsBuilder
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.ios.components.contracts.Component
import solutions.bellatrix.ios.configuration.IOSSettings
import solutions.bellatrix.ios.findstrategies.*
import solutions.bellatrix.ios.infrastructure.DriverService
import solutions.bellatrix.ios.services.AppService
import solutions.bellatrix.ios.services.ComponentCreateService
import solutions.bellatrix.ios.services.ComponentWaitService
import solutions.bellatrix.ios.waitstrategies.*
import java.util.*

open class IOSComponent : LayoutComponentValidationsBuilder(), Component {
    protected var wrappedElementHolder: MobileElement? = null
    override val wrappedElement: MobileElement
        get() {
            return try {
                wrappedElementHolder?.isDisplayed
                wrappedElementHolder ?: findElement()
            } catch (ex: StaleElementReferenceException) {
                findElement()
            }
        }
    var parentWrappedElement: MobileElement? = null
    var elementIndex = 0
    override lateinit var findStrategy: FindStrategy
    val wrappedDriver: IOSDriver<MobileElement>
    var appService: AppService
        protected set
    var componentCreateService: ComponentCreateService
        protected set
    var componentWaitService: ComponentWaitService
        protected set
    private val waitStrategies: MutableList<WaitStrategy>
    private val IOSSettings: IOSSettings
    override val componentName: String
        get() = String.format("%s (%s)", componentClass.simpleName, findStrategy.toString())

    fun waitToBe() {
        findElement()
    }

    fun hover() {
        HOVERING.broadcast(ComponentActionEventArgs(this))
        val action = Actions(wrappedDriver)
        action.moveToElement(findElement()).build().perform()
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

    fun <TElementType : IOSComponent> toExist(): TElementType {
        val waitStrategy = ToExistWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toNotExist(): TElementType {
        val waitStrategy = ToNotExistWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toBeVisible(): TElementType {
        val waitStrategy = ToBeVisibleWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toNotBeVisible(): TElementType {
        val waitStrategy = ToNotBeVisibleWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toBeClickable(): TElementType {
        val waitStrategy = ToBeClickableWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toBeDisabled(): TElementType {
        val waitStrategy = ToBeDisabledWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toHaveContent(): TElementType {
        val waitStrategy = ToHaveContentWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toExist(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToExistWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toNotExist(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToNotExistWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toBeVisible(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy =
            ToBeVisibleWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toNotBeVisible(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToNotBeVisibleWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toBeClickable(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy =
            ToBeClickableWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toBeDisabled(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToBeDisabledWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : IOSComponent> toHaveContent(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToHaveContentWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    inline fun <TElementType : IOSComponent, reified TWaitStrategy : WaitStrategy> to(element: TElementType): TElementType {
        val waitStrategy: TWaitStrategy = InstanceFactory.create()
        element.ensureState(waitStrategy)
        return element
    }

    inline fun <reified TComponent : IOSComponent> createByXPath(xpath: String): TComponent {
        return create<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : IOSComponent> createByName(name: String): TComponent {
        return create<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : IOSComponent> createByTag(tag: String): TComponent {
        return create<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : IOSComponent> byValueContaining(value: String): TComponent {
        return create<TComponent, ValueContainingFindStrategy>(value)
    }

    inline fun <reified TComponent : IOSComponent> byId(id: String): TComponent {
        return create<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : IOSComponent> byAccessibilityIdContaining(accessibilityId: String): TComponent {
        return create<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : IOSComponent> createAllByName(name: String): List<TComponent> {
        return createAll<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : IOSComponent> createAllByXPath(xpath: String): List<TComponent> {
        return createAll<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : IOSComponent> createAllByTag(tag: String): List<TComponent> {
        return createAll<TComponent,TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : IOSComponent> createAllByValueContaining(value: String): List<TComponent> {
        return createAll<TComponent, ValueContainingFindStrategy>(value)
    }

    inline fun <reified TComponent : IOSComponent> createAllById(id: String): List<TComponent> {
        return createAll<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : IOSComponent> createAllByAccessibilityIdContaining(accessibilityId: String): List<TComponent> {
        return createAll<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : IOSComponent, TFindStrategy : FindStrategy> create(value: String): TComponent {
        CREATING_ELEMENT.broadcast(ComponentActionEventArgs(this))
        findElement()
        val component: TComponent = InstanceFactory.create(value)
        component.findStrategy = findStrategy
        component.parentWrappedElement = wrappedElement
        CREATED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return component
    }

    inline fun <reified TComponent : IOSComponent, reified TFindStrategy : FindStrategy> createAll(value: String): List<TComponent> {
        CREATING_ELEMENTS.broadcast(ComponentActionEventArgs(this))
        findElement()
        val findStrategy = InstanceFactory.create<TFindStrategy>(value)
        val nativeElements = findStrategy.findAllElements(wrappedElement)
        val componentList: MutableList<TComponent> = ArrayList()
        for (i in 0 until nativeElements.stream().count()) {
            val component: TComponent = InstanceFactory.create()
            component.findStrategy = findStrategy
            component.elementIndex = i.toInt()
            component.parentWrappedElement = wrappedElement
            componentList.add(component)
        }
        CREATED_ELEMENTS.broadcast(ComponentActionEventArgs(this))
        return componentList
    }

    fun findElement(): MobileElement {
        if (waitStrategies.stream().count() == 0L) {
            waitStrategies.add(solutions.bellatrix.ios.waitstrategies.Wait.to().exists())
        }
        try {
            for (waitStrategy in waitStrategies) {
                componentWaitService.wait(this, waitStrategy)
            }
            wrappedElementHolder = findNativeElement()
            scrollToMakeElementVisible(wrappedElement)
            addArtificialDelay()
            waitStrategies.clear()
        } catch (ex: WebDriverException) {
            ex.debugStackTrace()
            print("\n\nThe component: \n Name: '${componentClass.simpleName}', \n Locator: '$findStrategy', \nWas not found on the page or didn't fulfill the specified conditions.\n\n")
        }
        RETURNING_WRAPPED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return wrappedElement
    }

    protected fun defaultClick(clicking: EventListener<ComponentActionEventArgs>, clicked: EventListener<ComponentActionEventArgs>) {
        clicking.broadcast(ComponentActionEventArgs(this))
        toExist<IOSComponent>().toBeClickable<IOSComponent>().waitToBe()
        findElement().click()
        clicked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultCheck(clicking: EventListener<ComponentActionEventArgs>, clicked: EventListener<ComponentActionEventArgs>) {
        clicking.broadcast(ComponentActionEventArgs(this))
        toExist<IOSComponent>().toBeClickable<IOSComponent>().waitToBe()
        if (!this.defaultGetCheckedAttribute()) {
            findElement().click()
        }
        clicked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultUncheck(clicking: EventListener<ComponentActionEventArgs>, clicked: EventListener<ComponentActionEventArgs>) {
        clicking.broadcast(ComponentActionEventArgs(this))
        toExist<IOSComponent>().toBeClickable<IOSComponent>().waitToBe()
        if (this.defaultGetCheckedAttribute()) {
            findElement().click()
        }
        clicked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultGetDisabledAttribute(): Boolean {
        val valueAttr = Optional.ofNullable(getAttribute("disabled")).orElse("false")
        return valueAttr.toLowerCase(Locale.ROOT) === "true"
    }

    protected fun defaultGetCheckedAttribute(): Boolean {
        val valueAttr = Optional.ofNullable(getAttribute("checked")).orElse("false")
        return valueAttr.toLowerCase(Locale.ROOT) === "true"
    }

    protected fun defaultGetText(): String {
        return Optional.ofNullable<String>(findElement().getText()).orElse("")
    }

    protected fun defaultSetText(settingValue: EventListener<ComponentActionEventArgs>, valueSet: EventListener<ComponentActionEventArgs>, value: String) {
        settingValue.broadcast(ComponentActionEventArgs(this))
        findElement().clear()
        findElement().sendKeys(value)
        valueSet.broadcast(ComponentActionEventArgs(this))
    }

    private fun findNativeElement(): MobileElement {
        return if (parentWrappedElement == null) {
            findStrategy.findAllElements(wrappedDriver).get(elementIndex)
        } else {
            findStrategy.findElement(parentWrappedElement!!)
        }
    }

    private fun addArtificialDelay() {
        if (IOSSettings.artificialDelayBeforeAction != 0) {
            try {
                Thread.sleep(IOSSettings.artificialDelayBeforeAction.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun scrollToMakeElementVisible(wrappedElement: MobileElement) {
        // createBy default scroll down to make the element visible.
        if (IOSSettings.automaticallyScrollToVisible) {
            scrollToVisible(wrappedElement, false)
        }
    }

    private fun scrollToVisible(wrappedElement: MobileElement, shouldWait: Boolean) {
        SCROLLING_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
        try {
            //            var js = (JavascriptExecutor)driver;
            //            Map<String, String> swipe = new HashMap<>();
            //            swipe.put("direction", "down"); // "up", "right", "left"
            //            swipe.put("element", element.getId());
            //            js.executeScript("mobile:swipe", swipe);
            val action = Actions(wrappedDriver)
            action.moveToElement(wrappedElement).perform()
            if (shouldWait) {
                Thread.sleep(500)
                toExist<IOSComponent>().waitToBe()
            }
        } catch (ex: ElementNotInteractableException) {
            ex.debugStackTrace()
        } catch (ex: InterruptedException) {
            ex.debugStackTrace()
        }
        SCROLLED_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
    }

    companion object {
        val HOVERING: EventListener<ComponentActionEventArgs> = EventListener()
        val HOVERED: EventListener<ComponentActionEventArgs> = EventListener()
        val SCROLLING_TO_VISIBLE: EventListener<ComponentActionEventArgs> = EventListener()
        val SCROLLED_TO_VISIBLE: EventListener<ComponentActionEventArgs> = EventListener()
        val RETURNING_WRAPPED_ELEMENT: EventListener<ComponentActionEventArgs> = EventListener()
        val CREATING_ELEMENT: EventListener<ComponentActionEventArgs> = EventListener()
        val CREATED_ELEMENT: EventListener<ComponentActionEventArgs> = EventListener()
        val CREATING_ELEMENTS: EventListener<ComponentActionEventArgs> = EventListener()
        val CREATED_ELEMENTS: EventListener<ComponentActionEventArgs> = EventListener()
    }

    init {
        waitStrategies = ArrayList()
        IOSSettings = ConfigurationService.get()
        appService = AppService
        componentCreateService = ComponentCreateService
        componentWaitService = ComponentWaitService
        wrappedDriver = DriverService.getWrappedIOSDriver()
    }
}