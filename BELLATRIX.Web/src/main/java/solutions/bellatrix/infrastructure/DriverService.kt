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
package solutions.bellatrix.infrastructure

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.Dimension
import org.openqa.selenium.InvalidArgumentException
import org.openqa.selenium.MutableCapabilities
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import java.util.*
import java.util.concurrent.TimeUnit

object DriverService {
    private var disposed: ThreadLocal<Boolean>

    //    private static ProxyService proxyService;
    @JvmStatic
    private val browserConfiguration: ThreadLocal<BrowserConfiguration>
    @JvmStatic
    private val customDriverOptions: ThreadLocal<HashMap<String, String>>
    @JvmStatic
    private val wrappedDriver: ThreadLocal<WebDriver>

    init {
        disposed = ThreadLocal()
        customDriverOptions = ThreadLocal()
        customDriverOptions.set(HashMap())
        browserConfiguration = ThreadLocal()
        wrappedDriver = ThreadLocal()
        disposed.set(false)
    }

    @JvmStatic
    fun customDriverOptions(): HashMap<String, String> {
        return customDriverOptions.get()
    }

    @JvmStatic
    fun addDriverOptions(key: String, value: String) {
        customDriverOptions.get()[key] = value
    }

    @JvmStatic
    fun wrappedDriver(): WebDriver {
        val driver = wrappedDriver.get()
        return driver
    }

    @JvmStatic
    fun browserConfiguration(): BrowserConfiguration {
        return browserConfiguration.get()
    }

    fun start(configuration: BrowserConfiguration): WebDriver {
        try {
            browserConfiguration.set(configuration)
            disposed.set(false)
            val driver: WebDriver = when (configuration.executionType) {
                ExecutionType.REGULAR -> initializeDriverRegularMode()
                ExecutionType.GRID -> initializeDriverGridMode()
                ExecutionType.SAUCE_LABS -> initializeDriverSauceLabsMode()
                ExecutionType.BROWSER_STACK -> initializeDriverBrowserStackMode()
                ExecutionType.CROSS_BROWSER_TESTING -> initializeDriverCrossBrowserTestingMode()
            }

            driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get<WebSettings>().timeoutSettings.pageLoadTimeout, TimeUnit.SECONDS)
            driver.manage().timeouts().setScriptTimeout(ConfigurationService.get<WebSettings>().timeoutSettings.scriptTimeout, TimeUnit.SECONDS)
            driver.manage().window().maximize()
            changeWindowSize(driver)
            wrappedDriver.set(driver)
            return driver
        } catch (ex: Exception) {
            return ChromeDriver()
        }
    }

    private fun initializeDriverRegularMode(): WebDriver {
        var driver: WebDriver = when (browserConfiguration.get().browser) {
            Browser.CHROME -> {
                WebDriverManager.chromedriver().setup()
                val chromeOptions = ChromeOptions()
                addDriverOptions(chromeOptions)
                chromeOptions.addArguments("--log-level=3")
                System.setProperty("webdriver.chrome.silentOutput", "true")
                val driver = ChromeDriver(chromeOptions)
                driver
            }
            Browser.CHROME_HEADLESS -> {
                WebDriverManager.chromedriver().setup()
                val chromeHeadlessOptions = ChromeOptions()
                addDriverOptions(chromeHeadlessOptions)
                chromeHeadlessOptions.setHeadless(true);
                chromeHeadlessOptions.addArguments("--log-level=3")
                System.setProperty("webdriver.chrome.silentOutput", "true")
                val driver = ChromeDriver(chromeHeadlessOptions)
                driver
            }
            Browser.FIREFOX -> {
                WebDriverManager.firefoxdriver().setup()
                val firefoxOptions = FirefoxOptions()
                addDriverOptions(firefoxOptions)
                val driver = FirefoxDriver(firefoxOptions)
                driver
            }
            Browser.FIREFOX_HEADLESS -> {
                WebDriverManager.firefoxdriver().setup()
                val firefoxHeadlessOptions = FirefoxOptions()
                addDriverOptions(firefoxHeadlessOptions)
                addDriverOptions(firefoxHeadlessOptions);
                val driver = FirefoxDriver(firefoxHeadlessOptions)
                driver
            }
            Browser.EDGE -> throw InvalidArgumentException("BELLATRIX doesn't support Edge. It will be supported with the official release of WebDriver 4.0")
            Browser.EDGE_HEADLESS -> throw InvalidArgumentException("BELLATRIX doesn't support Edge. It will be supported with the official release of WebDriver 4.0")
            Browser.OPERA -> throw InvalidArgumentException("BELLATRIX doesn't support Opera.")
            Browser.SAFARI -> throw InvalidArgumentException("BELLATRIX doesn't support Safari.")
            Browser.INTERNET_EXPLORER -> throw InvalidArgumentException("BELLATRIX doesn't support Internet Explorer.")
        }

        return driver
    }

    private fun <TOption : MutableCapabilities?> addDriverOptions(chromeOptions: TOption) {
        for ((k, v) in browserConfiguration.get().driverOptions) {
            chromeOptions?.setCapability(k, v)
        }
    }

    private fun initializeDriverCrossBrowserTestingMode(): WebDriver = FirefoxDriver()

    private fun initializeDriverBrowserStackMode(): WebDriver = FirefoxDriver()

    private fun initializeDriverSauceLabsMode(): WebDriver = FirefoxDriver()

    private fun initializeDriverGridMode(): WebDriver = FirefoxDriver()

    private fun changeWindowSize(wrappedDriver: WebDriver) {
        try {
            if (browserConfiguration().height != 0 && browserConfiguration().width != 0) {
                wrappedDriver.manage().window().size = Dimension(browserConfiguration().height, browserConfiguration().width)
            }
        } catch (ignored: Exception) {
        }
    }

    fun close() {
        if (disposed.get()) return
        wrappedDriver.get()?.close()
        customDriverOptions.get()?.clear()
        disposed.set(true)
    }
}