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
package solutions.bellatrix.services

import org.apache.commons.lang3.StringUtils
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver.TargetLocator
import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.WebDriver
import kotlin.jvm.JvmOverloads

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

    fun switchToFrame(frame: TargetLocator) {
        if (frame.activeElement() != null) {
            wrappedDriver.switchTo().frame(frame.activeElement())
        }
    }

    //    public void ClearSessionStorage() {
    //        var browserConfig = ServicesCollection.Current.Resolve < BrowserConfiguration > ();
    //        switch (browserConfig.BrowserType) {
    //            case BrowserType.NotSet:
    //                break;
    //            case BrowserType.Chrome:
    //            case BrowserType.ChromeHeadless:
    //                var chromeDriver = (ChromeDriver) WrappedDriver;
    //                chromeDriver.WebStorage.SessionStorage.Clear();
    //                break;
    //            case BrowserType.Firefox:
    //            case BrowserType.FirefoxHeadless:
    //                var firefoxDriver = (FirefoxDriver) WrappedDriver;
    //                firefoxDriver.WebStorage.SessionStorage.Clear();
    //                break;
    //            case BrowserType.InternetExplorer:
    //                var ieDriver = (InternetExplorerDriver) WrappedDriver;
    //                ieDriver.WebStorage.SessionStorage.Clear();
    //                break;
    //            case BrowserType.Edge:
    //            case BrowserType.EdgeHeadless:
    //                var edgeDriver = (EdgeDriver) WrappedDriver;
    //                edgeDriver.WebStorage.SessionStorage.Clear();
    //                break;
    //            case BrowserType.Opera:
    //                var operaDriver = (OperaDriver) WrappedDriver;
    //                operaDriver.WebStorage.SessionStorage.Clear();
    //                break;
    //            case BrowserType.Safari:
    //                var safariDriver = (SafariDriver) WrappedDriver;
    //                safariDriver.WebStorage.SessionStorage.Clear();
    //                break;
    //        }
    //    }
    //
    //    public void ClearLocalStorage() {
    //        var browserConfig = ServicesCollection.Current.Resolve < BrowserConfiguration > ();
    //        switch (browserConfig.BrowserType) {
    //            case BrowserType.NotSet:
    //                break;
    //            case BrowserType.Chrome:
    //            case BrowserType.ChromeHeadless:
    //                var chromeDriver = (ChromeDriver) WrappedDriver;
    //                chromeDriver.WebStorage.LocalStorage.Clear();
    //                break;
    //            case BrowserType.Firefox:
    //            case BrowserType.FirefoxHeadless:
    //                var firefoxDriver = (FirefoxDriver) WrappedDriver;
    //                firefoxDriver.WebStorage.LocalStorage.Clear();
    //                break;
    //            case BrowserType.InternetExplorer:
    //                var ieDriver = (InternetExplorerDriver) WrappedDriver;
    //                ieDriver.WebStorage.LocalStorage.Clear();
    //                break;
    //            case BrowserType.Edge:
    //            case BrowserType.EdgeHeadless:
    //                var edgeDriver = (EdgeDriver) WrappedDriver;
    //                edgeDriver.WebStorage.LocalStorage.Clear();
    //                break;
    //            case BrowserType.Opera:
    //                var operaDriver = (OperaDriver) WrappedDriver;
    //                operaDriver.WebStorage.LocalStorage.Clear();
    //                break;
    //            case BrowserType.Safari:
    //                var safariDriver = (SafariDriver) WrappedDriver;
    //                safariDriver.WebStorage.LocalStorage.Clear();
    //                break;
    //        }
    //    }
    fun waitForAjax() {
        val ajaxTimeout: Int = ConfigurationService.get<WebSettings>().timeoutSettings.waitForAjaxTimeout
        val sleepInterval: Int = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, ajaxTimeout.toLong(), sleepInterval.toLong())
        val javascriptExecutor = wrappedDriver as JavascriptExecutor
        webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return window.jQuery != undefined && jQuery.active == 0") as Boolean }
    }

    fun waitForAjaxRequest(requestPartialUrl: String?, additionalTimeoutInSeconds: Int = 0) {
        val ajaxTimeout: Int = ConfigurationService.get<WebSettings>().timeoutSettings.waitForAjaxTimeout
        val sleepInterval: Int = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, (ajaxTimeout + additionalTimeoutInSeconds).toLong(), sleepInterval.toLong())
        webDriverWait.until { d: WebDriver? ->
            val script = String.format("return performance.getEntriesByType('resource').filter(item => item.initiatorType == 'xmlhttprequest' && item.name.toLowerCase().includes('%s'))[0] !== undefined;", requestPartialUrl)
            val result = javascriptExecutor.executeScript(script) as String
            if (result === "True") {
                true
            }
            false
        }
    }

    fun waitUntilPageLoadsCompletely() {
        val waitUntilReadyTimeout: Int = ConfigurationService.get<WebSettings>().timeoutSettings.waitUntilReadyTimeout
        val sleepInterval: Int = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, waitUntilReadyTimeout.toLong(), sleepInterval.toLong())
        webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return document.readyState").toString() == "complete" }
    }

    fun waitForJavaScriptAnimations() {
        val waitForJavaScriptAnimationsTimeout: Int = ConfigurationService.get<WebSettings>().timeoutSettings.waitForJavaScriptAnimationsTimeout
        val sleepInterval: Int = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, waitForJavaScriptAnimationsTimeout.toLong(), sleepInterval.toLong())
        webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return jQuery && jQuery(':animated').length === 0") as Boolean }
    }

    fun waitForAngular() {
        val angularTimeout: Int = ConfigurationService.get<WebSettings>().timeoutSettings.waitForAngularTimeout
        val sleepInterval: Int = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(wrappedDriver, angularTimeout.toLong(), sleepInterval.toLong())
        val isAngular5 = javascriptExecutor.executeScript("return getAllAngularRootElements()[0].attributes['ng-version']") as String
        if (isAngular5.isBlank()) {
            webDriverWait.until { d: WebDriver? -> javascriptExecutor.executeScript("return window.getAllAngularTestabilities().findIndex(x=>!x.isStable()) === -1") as Boolean }
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