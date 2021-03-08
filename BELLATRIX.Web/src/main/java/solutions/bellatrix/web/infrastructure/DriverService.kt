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
package solutions.bellatrix.web.infrastructure

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.ie.InternetExplorerOptions
import org.openqa.selenium.opera.OperaDriver
import org.openqa.selenium.opera.OperaOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.web.configuration.GridSettings
import solutions.bellatrix.web.configuration.WebSettings
import solutions.bellatrix.web.services.ProxyServer
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

object DriverService {
    private var disposed: ThreadLocal<Boolean>
    private val browserConfiguration: ThreadLocal<BrowserConfiguration>
    private val customDriverOptions: ThreadLocal<HashMap<String, String>>
    private val wrappedDriver: ThreadLocal<WebDriver>

    init {
        disposed = ThreadLocal()
        customDriverOptions = ThreadLocal()
        customDriverOptions.set(HashMap())
        browserConfiguration = ThreadLocal()
        wrappedDriver = ThreadLocal()
        disposed.set(false)
    }

    fun customDriverOptions(): HashMap<String, String> {
        return customDriverOptions.get()
    }

    fun addDriverOptions(key: String, value: String) {
        customDriverOptions.get()[key] = value
    }

    fun wrappedDriver(): WebDriver {
        return wrappedDriver.get()
    }

    fun browserConfiguration(): BrowserConfiguration {
        return browserConfiguration.get()
    }

    fun start(configuration: BrowserConfiguration): WebDriver {
        browserConfiguration.set(configuration)
        disposed.set(false)
        val webSettings = ConfigurationService.get<WebSettings>()
        val executionType = webSettings.executionType
        var driver = if (executionType.toLowerCase().equals("regular")) {
            initializeDriverRegularMode()
        } else {
            val gridSettings: GridSettings = webSettings.gridSettings.first { g -> g.providerName.equals(executionType.toLowerCase()) }
            initializeDriverGridMode(gridSettings)
        }

        driver.manage().timeouts().pageLoadTimeout(ConfigurationService.get<WebSettings>().timeoutSettings.pageLoadTimeout, TimeUnit.SECONDS)
        driver.manage().timeouts().setScriptTimeout(ConfigurationService.get<WebSettings>().timeoutSettings.scriptTimeout, TimeUnit.SECONDS)
        driver.manage().window().maximize()
        changeWindowSize(driver)
        wrappedDriver.set(driver)

        return driver
    }

    private fun initializeDriverRegularMode(): WebDriver {
        val shouldCaptureHttpTraffic: Boolean = ConfigurationService.get<WebSettings>().shouldCaptureHttpTraffic
        val port: Int = ProxyServer.init()
        val proxyUrl = "127.0.0.1:$port"
        val proxyConfig = Proxy()
                .setHttpProxy(proxyUrl)
                .setSslProxy(proxyUrl)
                .setFtpProxy(proxyUrl)

        var driver: WebDriver = when (browserConfiguration.get().browser) {
            Browser.CHROME -> {
                WebDriverManager.chromedriver().setup()
                val chromeOptions = ChromeOptions()
                addDriverOptions(chromeOptions)
                chromeOptions.setAcceptInsecureCerts(true)
                chromeOptions.addArguments("--log-level=3")
                System.setProperty("webdriver.chrome.silentOutput", "true")
                if (shouldCaptureHttpTraffic) chromeOptions.setProxy(proxyConfig)
                val driver = ChromeDriver(chromeOptions)
                driver
            }
            Browser.CHROME_HEADLESS -> {
                WebDriverManager.chromedriver().setup()
                val chromeHeadlessOptions = ChromeOptions()
                addDriverOptions(chromeHeadlessOptions)
                chromeHeadlessOptions.setHeadless(true)
                chromeHeadlessOptions.setAcceptInsecureCerts(true)
                chromeHeadlessOptions.addArguments("--log-level=3")
                System.setProperty("webdriver.chrome.silentOutput", "true")
                if (shouldCaptureHttpTraffic) chromeHeadlessOptions.setProxy(proxyConfig)
                val driver = ChromeDriver(chromeHeadlessOptions)
                driver
            }
            Browser.FIREFOX -> {
                WebDriverManager.firefoxdriver().setup()
                val firefoxOptions = FirefoxOptions()
                addDriverOptions(firefoxOptions)
                firefoxOptions.setAcceptInsecureCerts(true)
                if (shouldCaptureHttpTraffic) firefoxOptions.setProxy(proxyConfig)
                val driver = FirefoxDriver(firefoxOptions)
                driver
            }
            Browser.FIREFOX_HEADLESS -> {
                WebDriverManager.firefoxdriver().setup()
                val firefoxHeadlessOptions = FirefoxOptions()
                addDriverOptions(firefoxHeadlessOptions)
                firefoxHeadlessOptions.setAcceptInsecureCerts(true)
                if (shouldCaptureHttpTraffic) firefoxHeadlessOptions.setProxy(proxyConfig)
                val driver = FirefoxDriver(firefoxHeadlessOptions)
                driver
            }
            Browser.EDGE -> {
                WebDriverManager.edgedriver().setup()
                val edgeOptions = EdgeOptions()
                addDriverOptions(edgeOptions)
                if (shouldCaptureHttpTraffic) edgeOptions.setProxy(proxyConfig)
                val driver = EdgeDriver(edgeOptions)
                driver
            }
            Browser.EDGE_HEADLESS -> {
                WebDriverManager.edgedriver().setup()
                val edgeOptions = EdgeOptions()
                addDriverOptions(edgeOptions)
                edgeOptions.setCapability("headless", true)
                if (shouldCaptureHttpTraffic) edgeOptions.setProxy(proxyConfig)
                val driver = EdgeDriver(edgeOptions)
                driver
            }
            Browser.OPERA -> {
                WebDriverManager.operadriver().setup()
                val operaOptions = OperaOptions()
                addDriverOptions(operaOptions)
                if (shouldCaptureHttpTraffic) operaOptions.setProxy(proxyConfig)
                val driver = OperaDriver(operaOptions)
                driver
            }
            Browser.SAFARI -> throw InvalidArgumentException("BELLATRIX doesn't support Safari.")
            Browser.INTERNET_EXPLORER -> {
                WebDriverManager.iedriver().setup()
                val internetExplorerOptions = InternetExplorerOptions()
                addDriverOptions(internetExplorerOptions)
                if (shouldCaptureHttpTraffic) internetExplorerOptions.setProxy(proxyConfig)
                val driver = InternetExplorerDriver(internetExplorerOptions)
                driver
            }
        }

        return driver
    }

    private fun <TOption : MutableCapabilities?> addDriverOptions(chromeOptions: TOption) {
        for ((k, v) in browserConfiguration.get().driverOptions) {
            chromeOptions?.setCapability(k, v)
        }
    }

    private fun initializeDriverGridMode(gridSettings: GridSettings): WebDriver {
        val caps = DesiredCapabilities()
        if (browserConfiguration.get().platform !== Platform.ANY) {
            caps.setCapability("platform", browserConfiguration.get().platform)
        }
        if (browserConfiguration.get().version != 0) {
            caps.setCapability("version", browserConfiguration.get().version)
        } else {
            caps.setCapability("version", "latest")
        }
        when (browserConfiguration.get().browser) {
            Browser.CHROME_HEADLESS, Browser.CHROME -> {
                val chromeOptions = ChromeOptions()
                addGridOptions(chromeOptions, gridSettings)
                caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions)
            }
            Browser.FIREFOX_HEADLESS, Browser.FIREFOX -> {
                val firefoxOptions = FirefoxOptions()
                addGridOptions(firefoxOptions, gridSettings)
                caps.setCapability(ChromeOptions.CAPABILITY, firefoxOptions)
            }
            Browser.OPERA -> {
                val operaOptions = OperaOptions()
                addGridOptions(operaOptions, gridSettings)
                caps.setCapability(ChromeOptions.CAPABILITY, operaOptions)
            }
            Browser.SAFARI -> {
                throw InvalidArgumentException("BELLATRIX doesn't support Safari.")
            }
            Browser.INTERNET_EXPLORER -> {
                val ieOptions = InternetExplorerOptions()
                addGridOptions(ieOptions, gridSettings)
                caps.setCapability(ChromeOptions.CAPABILITY, ieOptions)
            }
        }
        var driver = RemoteWebDriver(URL(gridSettings.url), caps)

        return driver
    }

    private fun <TOption : MutableCapabilities> addGridOptions(options: TOption, gridSettings: GridSettings) {
        for (entry in gridSettings.arguments) {
            for ((key, value) in entry) {
                if (key.startsWith("env_")) {
                    val envValue = System.getProperty(key.replace("env_", ""))
                    options.setCapability(key, envValue)
                } else {
                    options.setCapability(key, value)
                }
            }
        }
    }

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