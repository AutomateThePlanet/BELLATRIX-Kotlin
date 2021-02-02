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
package solutions.bellatrix.components

import solutions.bellatrix.infrastructure.DriverService.wrappedDriver
import solutions.bellatrix.findstrategies.FindStrategy
import solutions.bellatrix.services.JavaScriptService
import solutions.bellatrix.services.BrowserService
import solutions.bellatrix.services.ComponentCreateService
import solutions.bellatrix.services.ComponentWaitService
import solutions.bellatrix.waitstrategies.WaitStrategy
import solutions.bellatrix.configuration.WebSettings
import layout.LayoutAssertionsFactory
import org.apache.commons.lang3.StringEscapeUtils
import solutions.bellatrix.waitstrategies.ToExistsWaitStrategy
import solutions.bellatrix.waitstrategies.ToBeClickableWaitStrategy
import solutions.bellatrix.waitstrategies.ToBeVisibleWaitStrategy
import solutions.bellatrix.utilities.InstanceFactory
import solutions.bellatrix.findstrategies.IdFindStrategy
import solutions.bellatrix.findstrategies.CssFindStrategy
import solutions.bellatrix.findstrategies.ClassFindStrategy
import solutions.bellatrix.findstrategies.XPathFindStrategy
import solutions.bellatrix.findstrategies.LinkTextFindStrategy
import solutions.bellatrix.findstrategies.TagFindStrategy
import solutions.bellatrix.findstrategies.IdContainingFindStrategy
import solutions.bellatrix.findstrategies.InnerTextContainsFindStrategy
import org.openqa.selenium.*
import java.net.URLDecoder
import java.lang.InterruptedException
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.components.contracts.Component
import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.plugins.EventListener
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Function

open abstract class WebComponent : Component {
    override lateinit var wrappedElement: WebElement
    lateinit var parentWrappedElement: WebElement
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
        val VALIDATED_ACCEPT_IS_NULL = EventListener<ComponentActionEventArgs>()
        val VALIDATED_ACCEPT_IS = EventListener<ComponentActionEventArgs>()
        val VALIDATED_HREF_IS_SET = EventListener<ComponentActionEventArgs>()
        val VALIDATED_HREF_IS = EventListener<ComponentActionEventArgs>()
    }

    fun layout(): LayoutAssertionsFactory {
        return LayoutAssertionsFactory(this)
    }

    override val elementName: String
        get() = "$componentClass.simpleName ($findStrategy)"

    fun waitToBe() {
        findElement()
    }

    fun scrollToVisible() {
        scrollToVisible(findElement(), false)
    }

    fun setAttribute(name: String, value: String) {
        SETTING_ATTRIBUTE.broadcast(ComponentActionEventArgs(this))
        javaScriptService.execute("arguments[0].setAttribute('$name', '$value');", this)
        ATTRIBUTE_SET.broadcast(ComponentActionEventArgs(this))
    }

    fun focus() {
        FOCUSING.broadcast(ComponentActionEventArgs(this))
        javaScriptService.execute("window.focus();")
        javaScriptService.execute("arguments[0].focus();", findElement())
        FOCUSED.broadcast(ComponentActionEventArgs(this))
    }

    fun hover() {
        HOVERING.broadcast(ComponentActionEventArgs(this))
        javaScriptService.execute("arguments[0].onmouseover();", findElement())
        HOVERED.broadcast(ComponentActionEventArgs(this))
    }

    abstract override val componentClass: Class<*>

    override val location: Point
        get() = findElement().location

    override val size: Dimension
        get() = findElement().size

    val title: String
        get() = attribute("title")

    val tabIndex: String
        get() = attribute("tabindex")

    val accessKey: String
        get() = attribute("accesskey")

    val style: String
        get() = attribute("style")

    val dir: String
        get() = attribute("dir")

    val lang: String
        get() = attribute("lang")

    val htmlClass: String
        get() = attribute("class")

    fun attribute(name: String): String {
        return findElement().getAttribute(name)
    }

    fun cssValue(propertyName: String): String {
        return findElement().getCssValue(propertyName)
    }

    fun ensureState(waitStrategy: WaitStrategy) {
        waitStrategies.add(waitStrategy)
    }

    fun <TElementType : WebComponent> toExists(): TElementType {
        val waitStrategy = ToExistsWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeClickable(): TElementType {
        val waitStrategy = ToBeClickableWaitStrategy()
        ensureState(waitStrategy)
        return this as TElementType
    }

    fun <TElementType : WebComponent> toBeVisible(): TElementType {
        val waitStrategy = ToBeVisibleWaitStrategy()
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

    protected inline fun <reified TComponent : WebComponent> createById(id: String): TComponent {
        return create<TComponent, IdFindStrategy>(id)
    }

    protected inline fun <reified TComponent : WebComponent> createByCss(css: String): TComponent {
        return create<TComponent, CssFindStrategy>(css)
    }

    protected inline fun <reified TComponent : WebComponent> createByClass(cclass: String): TComponent {
        return create<TComponent, ClassFindStrategy>(cclass)
    }

    protected inline fun <reified TComponent : WebComponent> createByXPath(xpath: String): TComponent {
        return create<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : WebComponent> createByLinkText(linkText: String): TComponent {
        return create<TComponent, LinkTextFindStrategy>(linkText)
    }

    protected inline fun <reified TComponent : WebComponent> createByTag(tag: String): TComponent {
        return create<TComponent, TagFindStrategy>(tag)
    }

    protected inline fun <reified TComponent : WebComponent> createByIdContaining(idContaining: String): TComponent {
        return create<TComponent, IdContainingFindStrategy>(idContaining)
    }

    protected inline fun <reified TComponent : WebComponent> createByInnerTextContaining(innerText: String): TComponent {
        return create<TComponent, InnerTextContainsFindStrategy>(innerText)
    }

    protected inline fun <reified TComponent : WebComponent> createAllById(id: String): List<TComponent> {
        return createAll<TComponent, IdFindStrategy>(id)
    }

    protected inline fun <reified TComponent : WebComponent> createAllByCss(css: String): List<TComponent> {
        return createAll<TComponent, CssFindStrategy>(css)
    }

    protected inline fun <reified TComponent : WebComponent> createAllByClass(cclass: String): List<TComponent> {
        return createAll<TComponent, ClassFindStrategy>(cclass)
    }

    protected inline fun <reified TComponent : WebComponent> createAllByXPath(xpath: String): List<TComponent> {
        return createAll<TComponent, XPathFindStrategy>(xpath)
    }

    protected inline fun <reified TComponent : WebComponent> createAllByLinkText(linkText: String): List<TComponent> {
        return createAll<TComponent, LinkTextFindStrategy>(linkText)
    }

    protected inline fun <reified TComponent : WebComponent> createAllByTag(tag: String): List<TComponent> {
        return createAll<TComponent, TagFindStrategy>(tag)
    }

    protected inline fun <reified TComponent : WebComponent> createAllByIdContaining(idContaining: String): List<TComponent> {
        return createAll<TComponent, IdContainingFindStrategy>(idContaining)
    }

    protected inline fun <reified TComponent : WebComponent> createAllByInnerTextContaining(innerText: String): List<TComponent> {
        return createAll<TComponent, InnerTextContainsFindStrategy>(innerText)
    }

    protected inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> create(value: String): TComponent {
        CREATING_ELEMENT.broadcast(ComponentActionEventArgs(this))
        findElement()
        val findStrategy = InstanceFactory.create<TFindStrategy>(value)
        this.findStrategy = findStrategy
        this.parentWrappedElement = wrappedElement
        CREATED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return this as TComponent
    }

    protected inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> createAll(value: String): List<TComponent> {
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

    protected fun findElement(): WebElement {
        if (waitStrategies.stream().count() == 0L) {
            waitStrategies.add(ToExistsWaitStrategy.of())
        }
        try {
            for (waitStrategy in waitStrategies) {
                componentWaitService.wait(this, waitStrategy)
            }
            wrappedElement = findNativeElement()
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
            print(String.format("\n\nThe element: \n Name: '%s', \n Locator: '%s = %s', \nWas not found on the page or didn't fulfill the specified conditions.\n\n", componentClass.simpleName, findStrategy.toString(), findStrategy!!.value))
        }
        RETURNING_WRAPPED_ELEMENT.broadcast(ComponentActionEventArgs(this))
        return wrappedElement
    }

    protected fun defaultClick(clicking: EventListener<ComponentActionEventArgs>, clicked: EventListener<ComponentActionEventArgs>) {
        clicking.broadcast(ComponentActionEventArgs(this))
        toExists<WebComponent>().toBeClickable<WebComponent>().waitToBe()
        javaScriptService.execute("arguments[0].focus();arguments[0].click();", wrappedElement)
        clicked.broadcast(ComponentActionEventArgs(this))
    }

    protected fun setValue(gettingValue: EventListener<ComponentActionEventArgs>, gotValue: EventListener<ComponentActionEventArgs>, value: String) {
        gettingValue.broadcast(ComponentActionEventArgs(this))
        setAttribute("value", value)
        gotValue.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultGetValue(): String {
        return attribute("value") ?: ""
    }

    protected fun defaultGetMaxLength(): Int {
        return attribute("maxlength").toInt() ?: 0
    }

    protected fun defaultGetMinLength(): Int {
        return attribute("minlength").toInt() ?: 0
    }

    protected fun defaultGetSizeAttribute(): Int {
        return attribute("size").toInt() ?: 0
    }

    protected fun defaultGetHeightAttribute(): Int {
        return attribute("height").toInt() ?: 0
    }

    protected fun defaultGetWidthAttribute(): Int {
        return attribute("width").toInt() ?: 0
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

    protected fun defaultGetDisabledAttribute(): Boolean {
        return attribute("disabled").toBoolean() ?: false
    }

    protected fun defaultGetText(): String {
        return findElement().text ?: ""
    }

    protected fun defaultGetMinAttribute(): Int {
        return attribute("min").toInt() ?: 0
    }

    protected fun defaultGetMaxAttribute(): Int {
        return attribute("max").toInt() ?: 0
    }

    protected fun defaultGetStepAttribute(): Int {
        return attribute("step").toInt() ?: 0
    }

    protected fun defaultGetPlaceholderAttribute(): String {
        return attribute("placeholder") ?: ""
    }

    protected fun defaultGetAcceptAttribute(): String {
        return attribute("accept") ?: ""
    }

    protected fun defaultGetAutoCompleteAttribute(): Boolean {
        return attribute("autocomplete").toBoolean() ?: false
    }

    protected fun defaultGetReadonlyAttribute(): Boolean {
        return attribute("readonly").toBoolean() ?: false
    }

    protected fun defaultGetRequiredAttribute(): Boolean {
        return attribute("required").toBoolean() ?: false
    }

    protected fun defaultGetList(): String {
        return attribute("list") ?: ""
    }

    protected fun defaultGetHref(): String {
        return StringEscapeUtils.unescapeHtml4(URLDecoder.decode(Optional.ofNullable(attribute("href")).orElse(""), StandardCharsets.UTF_8.name()))
    }

    protected fun defaultSetText(settingValue: EventListener<ComponentActionEventArgs?>, valueSet: EventListener<ComponentActionEventArgs?>, value: String) {
        settingValue.broadcast(ComponentActionEventArgs(this))
        findElement().clear()
        findElement().sendKeys(value)
        valueSet.broadcast(ComponentActionEventArgs(this))
    }

    private fun findNativeElement(): WebElement {
        return if (parentWrappedElement == null) {
            wrappedDriver.findElements(findStrategy!!.convert())[elementIndex]
        } else {
            parentWrappedElement!!.findElements(findStrategy!!.convert())[elementIndex]
        }
    }

    private fun addArtificialDelay() {
        if (webSettings.artificialDelayBeforeAction != 0) {
            try {
                Thread.sleep(webSettings.artificialDelayBeforeAction.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun scrollToMakeElementVisible(wrappedElement: WebElement?) {
        // createBy default scroll down to make the element visible.
        if (webSettings.automaticallyScrollToVisible) {
            scrollToVisible(wrappedElement, false)
        }
    }

    private fun scrollToVisible(wrappedElement: WebElement?, shouldWait: Boolean) {
        SCROLLING_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
        try {
            javaScriptService.execute("arguments[0].scrollIntoView(true);", wrappedElement!!)
            if (shouldWait) {
                Thread.sleep(500)
                toExists<WebComponent>().waitToBe()
            }
        } catch (ex: ElementNotInteractableException) {
            print(ex)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        SCROLLED_TO_VISIBLE.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultValidateAcceptIsNull() {
        waitUntil({ d: SearchContext? -> defaultGetAcceptAttribute() == null }, String.format("The control's accept should be null but was '%s'.", defaultGetAcceptAttribute()))
        VALIDATED_ACCEPT_IS_NULL.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultValidateAcceptIs(value: String) {
        waitUntil({ d: SearchContext? -> defaultGetAcceptAttribute() == value }, String.format("The control's accept should be '%s' but was '%s'.", defaultGetAcceptAttribute()))
        VALIDATED_ACCEPT_IS.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultValidateHrefIs(value: String) {
        waitUntil({ d: SearchContext? -> defaultGetHref() == value }, String.format("The control's href should be '%s' but was '%s'.", value, defaultGetHref()))
        VALIDATED_HREF_IS.broadcast(ComponentActionEventArgs(this))
    }

    protected fun defaultValidateHrefIsSet() {
        waitUntil({ !defaultGetHref().isBlank() }, "The control's href shouldn't be empty but was.")
        VALIDATED_HREF_IS_SET.broadcast(ComponentActionEventArgs(this))
    }

    private fun waitUntil(waitCondition: Function<SearchContext, Boolean>, exceptionMessage: String) {
        val webDriverWait = WebDriverWait(wrappedDriver(), webSettings.timeoutSettings.validationsTimeout.toLong(), webSettings.timeoutSettings.sleepInterval.toLong())
        try {
            webDriverWait.until(waitCondition)
        } catch (ex: TimeoutException) {
            val validationExceptionMessage = "$exceptionMessage The test failed on URL: $browserService.url"
            throw TimeoutException(validationExceptionMessage, ex)
        }
    }
}