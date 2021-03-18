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
package solutions.bellatrix.web.services

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.support.ui.WebDriverWait
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.web.components.Frame
import solutions.bellatrix.web.configuration.WebSettings
import solutions.bellatrix.web.infrastructure.Browser
import solutions.bellatrix.web.infrastructure.BrowserConfiguration

object BrowserService : WebService() {
    private val javascriptExecutor: JavascriptExecutor
        get() = wrappedDriver as JavascriptExecutor

    val pageSource: String
        get() = wrappedDriver.pageSource
    val url: String
        get() = wrappedDriver.currentUrl
    val title: String
        get() = wrappedDriver.title

    fun back() {
        wrappedDriver.navigate().back()
    }

    fun forward() {
        wrappedDriver.navigate().forward()
    }

    fun refresh() {
        wrappedDriver.navigate().refresh()
    }

    fun switchToDefault() {
        wrappedDriver.switchTo().defaultContent()
    }

    fun switchToActive() {
        wrappedDriver.switchTo().activeElement()
    }

    fun switchToFirstBrowserTab() {
        wrappedDriver.switchTo().window(wrappedDriver.windowHandles.firstOrNull())
    }

    fun switchToLastTab() {
        wrappedDriver.switchTo().window(wrappedDriver.windowHandles.lastOrNull())
    }

    fun switchToTab(tabName: String) {
        wrappedDriver.switchTo().window(tabName)
    }

    fun switchToFrame(frame: Frame) {
        wrappedDriver.switchTo().frame(frame.findElement())
    }

    fun clearSessionStorage() {
        val browserConfig = InstanceFactory.create<BrowserConfiguration>()
        when (browserConfig.browser) {
            Browser.CHROME, Browser.CHROME_HEADLESS -> {
                val chromeDriver = wrappedDriver as ChromeDriver
                chromeDriver.sessionStorage.clear()
            }
            Browser.FIREFOX, Browser.FIREFOX_HEADLESS -> {
                val firefoxDriver = wrappedDriver as FirefoxDriver
                firefoxDriver.sessionStorage.clear()
            }
            Browser.INTERNET_EXPLORER -> {
                val ieDriver = wrappedDriver as InternetExplorerDriver
                (ieDriver as JavascriptExecutor).executeScript("sessionStorage.clear()")
            }
            Browser.EDGE, Browser.EDGE_HEADLESS -> {
                val edgeDriver = wrappedDriver as EdgeDriver
                (edgeDriver as JavascriptExecutor).executeScript("sessionStorage.clear()")
            }
            Browser.OPERA -> {
                val operaDriver = wrappedDriver as OperaDriver
                operaDriver.sessionStorage.clear()
            }
            Browser.SAFARI -> {
                val safariDriver = wrappedDriver as SafariDriver
                (safariDriver as JavascriptExecutor).executeScript("sessionStorage.clear()")
            }
        }
    }

    fun clearLocalStorage() {
        val browserConfig = InstanceFactory.create<BrowserConfiguration>()
        when (browserConfig.browser) {
            Browser.CHROME, Browser.CHROME_HEADLESS -> {
                val chromeDriver = wrappedDriver as ChromeDriver
                chromeDriver.localStorage.clear()
            }
            Browser.FIREFOX, Browser.FIREFOX_HEADLESS -> {
                val firefoxDriver = wrappedDriver as FirefoxDriver
                firefoxDriver.localStorage.clear()
            }
            Browser.INTERNET_EXPLORER -> {
                val ieDriver = wrappedDriver as InternetExplorerDriver
                (ieDriver as JavascriptExecutor).executeScript("localStorage.clear()")
            }
            Browser.EDGE, Browser.EDGE_HEADLESS -> {
                val edgeDriver = wrappedDriver as EdgeDriver
                (edgeDriver as JavascriptExecutor).executeScript("localStorage.clear()")
            }
            Browser.OPERA -> {
                val operaDriver = wrappedDriver as OperaDriver
                operaDriver.localStorage.clear()
            }
            Browser.SAFARI -> {
                val safariDriver = wrappedDriver as SafariDriver
                (safariDriver as JavascriptExecutor).executeScript("localStorage.clear()")
            }
        }
    }

    fun waitForAjax() {
        val ajaxTimeout = ConfigurationService.get<WebSettings>().timeoutSettings.waitForAjaxTimeout
        val sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, ajaxTimeout, sleepInterval)
        val javascriptExecutor = wrappedDriver as JavascriptExecutor
        webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return window.jQuery != undefined && jQuery.active == 0") as Boolean }
    }

    fun waitForAjaxRequest(requestPartialUrl: String?, additionalTimeoutInSeconds: Int = 0) {
        val ajaxTimeout = ConfigurationService.get<WebSettings>().timeoutSettings.waitForAjaxTimeout
        val sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, (ajaxTimeout + additionalTimeoutInSeconds), sleepInterval)
        webDriverWait.until {
            val script = String.format("return performance.getEntriesByType('resource').filter(item => item.initiatorType == 'xmlhttprequest' && item.name.toLowerCase().includes('%s'))[0] !== undefined;", requestPartialUrl)
            val result = javascriptExecutor.executeScript(script) as String
            if (result === "True") {
                return@until true
            }
            false
        }
    }

    fun waitUntilPageLoadsCompletely() {
        val waitUntilReadyTimeout = ConfigurationService.get<WebSettings>().timeoutSettings.waitUntilReadyTimeout
        val sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, waitUntilReadyTimeout, sleepInterval)
        webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return document.readyState").toString() == "complete" }
    }

    fun waitForJavaScriptAnimations() {
        val waitForJavaScriptAnimationsTimeout = ConfigurationService.get<WebSettings>().timeoutSettings.waitForJavaScriptAnimationsTimeout
        val sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, waitForJavaScriptAnimationsTimeout, sleepInterval)
        webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return jQuery && jQuery(':animated').length === 0") as Boolean }
    }

    fun waitForAngular() {
        val angularTimeout = ConfigurationService.get<WebSettings>().timeoutSettings.waitForAngularTimeout
        val sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, angularTimeout, sleepInterval)
        val isAngular5 = javascriptExecutor.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']") as String
        if (isAngular5.isBlank()) {
            webDriverWait.until { javascriptExecutor.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1") as Boolean }
        } else {
            val isAngularDefined = javascriptExecutor.executeScript("return window.angular === undefined") as Boolean
            if (!isAngularDefined) {
                val isAngularInjectorUnDefined = javascriptExecutor.executeScript("return angular.element(document).injector() === undefined") as Boolean
                if (!isAngularInjectorUnDefined) {
                    webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return angular.element(document).injector().get('\$http').pendingRequests.length === 0") as Boolean }
                }
            }
        }
    }
}