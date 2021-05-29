/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required createBy applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package solutions.bellatrix.web.components

import layout.LayoutComponentValidationsBuilder
import org.apache.commons.text.StringEscapeUtils
import org.openqa.selenium.*
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.core.utilities.Log
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.web.components.contracts.*
import solutions.bellatrix.web.configuration.WebSettings
import solutions.bellatrix.web.findstrategies.*
import solutions.bellatrix.web.infrastructure.DriverService.wrappedDriver
import solutions.bellatrix.web.services.BrowserService
import solutions.bellatrix.web.services.ComponentCreateService
import solutions.bellatrix.web.services.ComponentWaitService
import solutions.bellatrix.web.services.JavaScriptService
import solutions.bellatrix.web.waitstrategies.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.*

abstract class WebComponent : LayoutComponentValidationsBuilder(), Component, ComponentStyle, ComponentTitle, ComponentHtmlClass, ComponentVisible, ComponentTabIndex, ComponentAccessKey, ComponentDir, ComponentLang {
    protected var wrappedElementHolder: WebElement? = null
    override val wrappedElement: WebElement
        get() {
            return try {
                wrappedElementHolder?.isDisplayed
                wrappedElementHolder ?: findElement()
            } catch (ex: StaleElementReferenceException) {
                findElement()
            } catch (ex: NoSuchElementException) {
                findElement()
            }
        }
    var parentWrappedElement: WebElement? = null
    var elementIndex = 0
    override lateinit var findStrategy: FindStrategy
    val wrappedDriver: WebDriver
    var javaScriptService: JavaScriptService
        protected set
    var browserService: BrowserService
        protected set
    var componentCreateService: ComponentCreateService
        protected set
    var componentWaitService: ComponentWaitService
        protected set
    private val waitStrategies: MutableList<WaitStrategy>
    private val webSettings: WebSettings

    init {
        waitStrategies = ArrayList()
        webSettings = ConfigurationService.get<WebSettings>()
        javaScriptService = JavaScriptService
        browserService = BrowserService
        componentCreateService = ComponentCreateService
        componentWaitService = ComponentWaitService
        wrappedDriver = wrappedDriver()
    }

    companion object {
        val HOVERING = EventListener<ComponentActionEventArgs>()
        val HOVERED = EventListener<ComponentActionEventArgs>()
        val FOCUSING = EventListener<ComponentActionEventArgs>()
        val FOCUSED = EventListener<ComponentActionEventArgs>()
        val SCROLLING_TO_VISIBLE = EventListener<ComponentActionEventArgs>()
        val SCROLLED_TO_VISIBLE = EventListener<ComponentActionEventArgs>()
        val SETTING_ATTRIBUTE = EventListener<ComponentActionEventArgs>()
        val ATTRIBUTE_SET = EventListener<ComponentActionEventArgs>()
        val RETURNING_WRAPPED_ELEMENT = EventListener<ComponentActionEventArgs>()
        val CREATING_ELEMENT = EventListener<ComponentActionEventArgs>()
        val CREATED_ELEMENT = EventListener<ComponentActionEventArgs>()
        val CREATING_ELEMENTS = EventListener<ComponentActionEventArgs>()
        val CREATED_ELEMENTS = EventListener<ComponentActionEventArgs>()
    }

    override val componentName: String
        get() = "${componentClass.simpleName} ($findStrategy)"

    fun waitToBe() {
        findElement()
    }

    fun scrollToVisible() {
        scrollToVisible(findElement(), false)
    }

    fun setAttribute(name: String, value: String) {
        SETTING_ATTRIBUTE.broadcast(ComponentActionEventArgs(this))
        javaScriptService.execute("arguments[0].setAttribute('$name', '$value');", findElement())
        ATTRIBUTE_SET.broadcast(ComponentActionEventArgs(this))
    }

    fun focus() {
        FOCUSING.broadcast(ComponentActionEventArgs(this))
        javaScriptService.execute("window.focus();")
        javaScriptService.execute("arguments[0].focus();", findElement())
        FOCUSED.broadcast(ComponentActionEventArgs(this))
    }

    protected fun hover() {
        HOVERING.broadcast(ComponentActionEventArgs(this))
        javaScriptService.execute("arguments[0].onmouseover();", findElement())
        HOVERED.broadcast(ComponentActionEventArgs(this))
    }

    abstract override val componentClass: Class<*>

    override val location: Point
        get() = findElement().location

    override val size: Dimension
        get() = findElement().size

    override val title: String
        get() = attribute("title") ?: ""

    override val tabIndex: String
        get() = attribute("tabindex") ?: ""

    override val accessKey: String
        get() = attribute("accesskey") ?: ""

    override val style: String
        get() = attribute("style") ?: ""

    override val dir: String
        get() = attribute("dir") ?: ""

    override val lang: String
        get() = attribute("lang") ?: ""

    override val htmlClass: String
        get() = attribute("class") ?: ""

    override val isVisible: Boolean
        get() = findElement().isDisplayed

    fun attribute(name: String): String? {
        return findElement().getAttribute(name)
    }

    fun cssValue(propertyName: String): String {
        return findElement().getCssValue(propertyName)
    }

    fun ensureState(waitStrategy: WaitStrategy) {
        waitStrategies.add(waitStrategy)
    }

    fun <TElementType : WebComponent> toExist(): TElementType {
        val waitStrategy = ToExistWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toNotExist(): TElementType {
        val waitStrategy = ToNotExistWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeVisible(): TElementType {
        val waitStrategy = ToBeVisibleWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toNotBeVisible(): TElementType {
        val waitStrategy = ToNotBeVisibleWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeClickable(): TElementType {
        val waitStrategy = ToBeClickableWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeDisabled(): TElementType {
        val waitStrategy = ToBeDisabledWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toHaveContent(): TElementType {
        val waitStrategy = ToHaveContentWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toExist(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToExistWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toNotExist(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToNotExistWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeVisible(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToBeVisibleWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toNotBeVisible(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToNotBeVisibleWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeClickable(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToBeClickableWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeDisabled(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToBeDisabledWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toHaveContent(timeoutInterval: Long, sleepInterval: Long): TElementType {
        val waitStrategy = ToHaveContentWaitStrategy(timeoutInterval, sleepInterval)
        ensureState(waitStrategy)
        return this as TElementType
    }

    inline fun <reified TElementType : WebComponent, reified TWaitStrategy : WaitStrategy> to(): TElementType {
        val waitStrategy = InstanceFactory.create<TWaitStrategy>()
        this.ensureState(waitStrategy)
        return this as TElementType
    }

//    protected inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> create(value: String): TComponent {
//        return create<TComponent, TFindStrategy>(value)
//    }
//
//    protected inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> createAll(findStrategyClass: Class<TFindStrategy>?, componentClass: Class<TComponent>?, vararg args: Any?): List<TComponent> {
//        val findStrategy: Unit = InstanceFactory.create(findStrategyClass, args)
//        return createAll(componentClass, findStrategy)
//    }

    inline fun <reified TComponent : WebComponent> createById(id: String): TComponent {
        return create<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : WebComponent> createByCss(css: String): TComponent {
        return create<TComponent, CssFindStrategy>(css)
    }

    inline fun <reified TComponent : WebComponent> createByClass(cclass: String): TComponent {
        return create<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : WebComponent> createByXPath(xpath: String): TComponent {
        return create<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : WebComponent> createByLinkText(linkText: String): TComponent {
        return create<TComponent, LinkTextFindStrategy>(linkText)
    }

    inline fun <reified TComponent : WebComponent> createByTag(tag: String): TComponent {
        return create<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : WebComponent> createByIdContaining(idContaining: String): TComponent {
        return create<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : WebComponent> createByInnerTextContaining(innerText: String): TComponent {
        return create<TComponent, InnerTextContainsFindStrategy>(innerText)
    }

    inline fun <reified TComponent : WebComponent> createAllById(id: String): List<TComponent> {
        return createAll<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : WebComponent> createAllByCss(css: String): List<TComponent> {
        return createAll<TComponent, CssFindStrategy>(css)
    }

    inline fun <reified TComponent : WebComponent> createAllByClass(cclass: String): List<TComponent> {
        return createAll<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : WebComponent> createAllByXPath(xpath: String): List<TComponent> {
        return createAll<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : WebComponent> createAllByLinkText(linkText: String): List<TComponent> {
        return createAll<TComponent, LinkTextFindStrategy>(linkText)
    }

    inline fun <reified TComponent : WebComponent> createAllByTag(tag: String): List<TComponent> {
        return createAll<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : WebComponent> createAllByIdContaining(idContaining: String): List<TComponent> {
        return createAll<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : WebComponent> createAllByInnerTextContaining(innerText: String): List<TComponent> {
        return createAll<TComponent, InnerTextContainsFindStrategy>(innerText)
    }

    inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> create(value: String): TComponent {
        CREATING_ELEMENT.broadcast(ComponentActionEventArgs(this))
        findElement()
        val findStrategy = InstanceFactory.create<TFindStrategy>(value)
        this.findStrategy = findStrategy
        this.parentWrappedElement = wrappedElement
        CREATED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return this as TComponent
    }

    inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> createAll(value: String): List<TComponent> {
        CREATING_ELEMENTS.broadcast(ComponentActionEventArgs(this))
        val findStrategy = InstanceFactory.create<TFindStrategy>(value)
        findElement()
        val nativeElements = wrappedElement.findElements(findStrategy.convert())
        val componentList = mutableListOf<TComponent>()
        for (i in 0 until nativeElements.count()) {
            val component: TComponent = InstanceFactory.create()
            component.findStrategy = findStrategy
            component.elementIndex = i
            component.parentWrappedElement = wrappedElement
            componentList.add(component)
        }
        CREATED_ELEMENTS.broadcast(ComponentActionEventArgs(this))
        return componentList
    }

    fun findElement(): WebElement {
        if (waitStrategies.stream().count() == 0L) {
            waitStrategies.add(ToExistWaitStrategy.of())
        }
        try {
            for (waitStrategy in waitStrategies) {
                componentWaitService.wait(this, waitStrategy)
            }
            wrappedElementHolder = findNativeElement()
            scrollToMakeElementVisible(wrappedElement)
            if (webSettings.waitUntilReadyOnElementFound) {
                browserService.waitForAjax()
            }
            if (webSettings.waitForAngular) {
                browserService.waitForAngular()
            }
            addArtificialDelay()
            waitStrategies.clear()
        } catch (ex: WebDriverException) {
            Log.error("%n%nThe component: %n" +
                              "     Type: \"\u001B[1m%s\u001B[0m\"%n" +
                              "  Locator: \"\u001B[1m%s\u001B[0m\"%n" +
                              "Was not found on the page or didn't fulfill the specified conditions.%n%n",
                      this::class.simpleName, findStrategy.toString());
            throw ex;
        }
        RETURNING_WRAPPED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return wrappedElement
    }


    private fun clickInternal() {
        val toBeClickableTimeout = webSettings.timeoutSettings.elementToBeClickableTimeout
        val sleepInterval = webSettings.timeoutSettings.sleepInterval
        val wait: FluentWait<WebDriver> = FluentWait(wrappedDriver)
            .withTimeout(Duration.ofSeconds(toBeClickableTimeout))
            .pollingEvery(Duration.ofSeconds(if (sleepInterval > 0) sleepInterval else 1))
        try {
            wait.until { x: WebDriver? -> tryClick() }
        } catch (e: TimeoutException) {
            toExist<WebComponent>().toBeClickable<WebComponent>().findElement().click()
        }
    }

    private fun tryClick(): Boolean {
        return try {
            toExist<WebComponent>().toBeClickable<WebComponent>().findElement().click()
            true
        } catch (e: ElementNotInteractableException) {
            false
        } catch (e: WebDriverException) {
            toExist<WebComponent>().toBeClickable<WebComponent>().waitToBe()
            false
        }
    }

    protected fun defaultClick(clicking: EventListener<ComponentActionEventArgs>, clicked: EventListener<ComponentActionEventArgs>) {
        clicking.broadcast(ComponentActionEventArgs(this))
        toExist<WebComponent>().toBeClickable<WebComponent>().waitToBe()
        clickInternal()
        clicked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultCheck(checking: EventListener<ComponentActionEventArgs>, checked: EventListener<ComponentActionEventArgs>) {
        checking.broadcast(ComponentActionEventArgs(this))
        toExist<WebComponent>().toBeClickable<WebComponent>().waitToBe()
        if (!findElement().isSelected) {
            clickInternal()
        }
        checked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultUncheck(unchecking: EventListener<ComponentActionEventArgs>, unchecked: EventListener<ComponentActionEventArgs>) {
        unchecking.broadcast(ComponentActionEventArgs(this))
        toExist<WebComponent>().toBeClickable<WebComponent>().waitToBe()
        if (findElement().isSelected) {
            clickInternal()
        }
        unchecked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun setValue(settingValue: EventListener<ComponentActionEventArgs>, valueSet: EventListener<ComponentActionEventArgs>, value: String) {
        settingValue.broadcast(ComponentActionEventArgs(this))
        javaScriptService.execute("arguments[0].value = '$value';", findElement())
        valueSet.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultGetValue(): String {
        return attribute("value") ?: ""
    }

    protected fun defaultGetRowsAttribute(): Int? {
        return try {
            attribute("rows")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetColsAttribute(): Int? {
        return try {
            attribute("cols")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetMaxLengthAttribute(): Int? {
        return try {
            attribute("maxlength")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetMinLengthAttribute(): Int? {
        return try {
            attribute("minlength")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetSizeAttribute(): Int? {
        return try {
            attribute("size")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetHeightAttribute(): Int? {
        return try {
            attribute("height")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetWidthAttribute(): Int? {
        return try {
            attribute("width")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetInnerHtmlAttribute(): String {
        return attribute("innerHTML") ?: ""
    }

    protected fun defaultGetForAttribute(): String {
        return attribute("for") ?: ""
    }

    protected fun defaultGetTargetAttribute(): String {
        return attribute("target") ?: ""
    }

    protected fun defaultGetRelAttribute(): String {
        return attribute("rel") ?: ""
    }

    protected fun defaultGetName(): String {
        return attribute("name") ?: ""
    }

    protected fun defaultGetDisabledAttribute(): Boolean {
        return attribute("disabled")?.toBoolean() ?: false
    }

    protected fun defaultGetText(): String {
        return try {
            Optional.ofNullable<String>(wrappedElement.text).orElse("")
        } catch (e: StaleElementReferenceException) {
            Optional.ofNullable(findElement().text).orElse("")
        }
    }

    protected fun defaultGetMinAttribute(): Double? {
        return try {
            attribute("min")?.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetMinAttributeAsString(): String {
        return attribute("min") ?: ""
    }

    protected fun defaultGetMaxAttribute(): Double? {
        return try {
            attribute("max")?.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetMaxAttributeAsString(): String {
        return attribute("max") ?: ""
    }

    protected fun defaultGetStepAttribute(): Double? {
        return try {
            attribute("step")?.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    protected fun defaultGetPlaceholderAttribute(): String {
        return attribute("placeholder") ?: ""
    }

    protected fun defaultGetAcceptAttribute(): String {
        return attribute("accept") ?: ""
    }

    protected fun defaultGetAutoCompleteAttribute(): Boolean {
        return attribute("autocomplete")?.toBoolean() ?: false
    }

    protected fun defaultGetReadonlyAttribute(): Boolean {
        return attribute("readonly")?.toBoolean() ?: false
    }

    protected fun defaultGetRequiredAttribute(): Boolean {
        return attribute("required")?.toBoolean() ?: false
    }

    protected fun defaultGetMultipleAttribute(): Boolean {
        return attribute("multiple")?.toBoolean() ?: false
    }

    protected fun defaultGetList(): String {
        return attribute("list") ?: ""
    }

    protected fun defaultGetSrcAttribute(): String {
        return attribute("src") ?: ""
    }

    protected fun defaultGetLongDescAttribute(): String {
        return attribute("longdesc") ?: ""
    }

    protected fun defaultGetAltAttribute(): String {
        return attribute("alt") ?: ""
    }

    protected fun defaultGetSizesSetAttribute(): String {
        return attribute("sizes") ?: ""
    }

    protected fun defaultGetSrcSetAttribute(): String {
        return attribute("srcset") ?: ""
    }

    protected fun defaultGetSpellCheckAttribute(): Boolean {
        return attribute("spellcheck")?.toBoolean() ?: false
    }

    protected fun defaultGetWrapAttribute(): String {
        return attribute("wrap") ?: ""
    }

    protected fun defaultGetHref(): String {
        return StringEscapeUtils.unescapeHtml4(URLDecoder.decode(Optional.ofNullable(attribute("href")).orElse(""), StandardCharsets.UTF_8.name()))
    }

    protected fun defaultSetText(settingValue: EventListener<ComponentActionEventArgs>, valueSet: EventListener<ComponentActionEventArgs>, value: String) {
        settingValue.broadcast(ComponentActionEventArgs(this, value))
        wrappedElement.clear()
        wrappedElement.sendKeys(value)
        valueSet.broadcast(ComponentActionEventArgs(this, value))
    }

    protected fun defaultSelectByText(settingValue: EventListener<ComponentActionEventArgs>, valueSet: EventListener<ComponentActionEventArgs>, value: String) {
        settingValue.broadcast(ComponentActionEventArgs(this, value))
        Select(findElement()).selectByVisibleText(value)
        valueSet.broadcast(ComponentActionEventArgs(this, value))
    }

    protected fun defaultSelectByIndex(settingValue: EventListener<ComponentActionEventArgs>, valueSet: EventListener<ComponentActionEventArgs>, value: Int) {
        settingValue.broadcast(ComponentActionEventArgs(this, value.toString()))
        Select(findElement()).selectByIndex(value)
        valueSet.broadcast(ComponentActionEventArgs(this, value.toString()))
    }

    private fun findNativeElement(): WebElement {
        return if (parentWrappedElement == null) {
            wrappedDriver.findElements(findStrategy.convert())[elementIndex]
        } else {
            parentWrappedElement!!.findElements(findStrategy.convert())[elementIndex]
        }
    }

    private fun addArtificialDelay() {
        if (webSettings.artificialDelayBeforeAction != 0) {
            try {
                Thread.sleep(webSettings.artificialDelayBeforeAction.toLong())
            } catch (e: InterruptedException) {
                e.debugStackTrace()
            }
        }
    }

    private fun scrollToMakeElementVisible(wrappedElement: WebElement) {
        // createBy default scroll down to make the element visible.
        if (webSettings.automaticallyScrollToVisible) {
            scrollToVisible(wrappedElement, false)
        }
    }

    private fun scrollToVisible(wrappedElement: WebElement, shouldWait: Boolean) {
        SCROLLING_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
        try {
            javaScriptService.execute("arguments[0].scrollIntoView(true);", wrappedElement)
            if (shouldWait) {
                Thread.sleep(500)
                toExist<WebComponent>().waitToBe()
            }
        } catch (ex: ElementNotInteractableException) {
            ex.debugStackTrace()
        } catch (e: InterruptedException) {
            e.debugStackTrace()
        }
        SCROLLED_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
    }
}