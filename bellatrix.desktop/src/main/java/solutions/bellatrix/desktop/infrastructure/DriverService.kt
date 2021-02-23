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
package solutions.bellatrix.desktop.infrastructure

import io.appium.java_client.windows.WindowsDriver
import org.openqa.selenium.*
import org.openqa.selenium.remote.DesiredCapabilities
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.desktop.configuration.DesktopSettings
import solutions.bellatrix.desktop.configuration.GridSettings
import java.io.File
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.TimeUnit

object DriverService {
    private var disposed: ThreadLocal<Boolean> = ThreadLocal()
    private var appConfiguration: ThreadLocal<AppConfiguration> = ThreadLocal()
    private var customDriverOptions: ThreadLocal<HashMap<String, String>> = ThreadLocal()
    private var wrappedDriver: ThreadLocal<WindowsDriver<WebElement>> = ThreadLocal()
    fun getCustomDriverOptions(): HashMap<String, String> {
        return customDriverOptions.get()
    }

    fun addDriverOptions(key: String, value: String) {
        customDriverOptions.get()[key] = value
    }

    @JvmStatic
    fun getWrappedDriver(): WindowsDriver<WebElement> {
        return wrappedDriver.get()
    }

    fun getAppConfiguration(): AppConfiguration {
        return appConfiguration.get()
    }

    fun start(configuration: AppConfiguration): WindowsDriver<WebElement>? {
        appConfiguration.set(configuration)
        disposed.set(false)

        val desktopSettings = ConfigurationService.get<DesktopSettings>()
        val executionType: String = desktopSettings.executionType
        val driver: WindowsDriver<WebElement> = if (executionType.equals("regular")) {
            initializeDriverRegularMode(desktopSettings.serviceUrl)
        } else {
            val gridSettings = desktopSettings.gridSettings.stream().filter { g -> g.providerName.equals(executionType.toLowerCase()) }.findFirst()
            initializeDriverGridMode(desktopSettings.gridSettings.stream().filter { g -> g.providerName.equals(executionType.toLowerCase()) }.findFirst().get())
        }
        driver.manage().timeouts().implicitlyWait(desktopSettings.timeoutSettings.implicitWaitTimeout, TimeUnit.SECONDS)
        driver.manage().window().maximize()
        changeWindowSize(driver)
        wrappedDriver.set(driver)
        return driver
    }

    private fun initializeDriverGridMode(gridSettings: GridSettings): WindowsDriver<WebElement> {
        val caps = DesiredCapabilities()
        caps.setCapability("platform", Platform.WIN10)
        caps.setCapability("version", "latest")
        var driver: WindowsDriver<WebElement> = try {
            WindowsDriver<WebElement>(URL(gridSettings.url), caps)
        } catch (e: MalformedURLException) {
            e.debugStackTrace()
            WindowsDriver<WebElement>(URL(gridSettings.url), caps)
        }
        return driver
    }

    private fun initializeDriverRegularMode(serviceUrl: String): WindowsDriver<WebElement> {
        val caps = DesiredCapabilities()
        caps.setCapability("app", getAppConfiguration().appPath)
        caps.setCapability("deviceName", "WindowsPC");
        caps.setCapability("platformName", "Windows");
        caps.setCapability("appWorkingDir", File(getAppConfiguration().appPath).getParent());
        addDriverOptions<DesiredCapabilities>(caps)
        return WindowsDriver<WebElement>(URL(serviceUrl), caps)
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

    private fun <TOption : MutableCapabilities?> addDriverOptions(options: TOption) {
        for (optionKey in appConfiguration.get().appiumOptions.keys) {
            options?.setCapability(optionKey, appConfiguration.get().appiumOptions[optionKey])
        }
    }

    private fun changeWindowSize(wrappedDriver: WebDriver) {
        try {
            if (getAppConfiguration().height != 0 && getAppConfiguration().width != 0) {
                wrappedDriver.manage().window().size = Dimension(getAppConfiguration().height, getAppConfiguration().width)
            }
        } catch (ignored: Exception) {
        }
    }

    fun close() {
        if (disposed.get()) {
            return
        }
        if (wrappedDriver.get() != null) {
            wrappedDriver.get().close()
            customDriverOptions.get().clear()
        }
        disposed.set(true)
    }

    init {
        disposed = ThreadLocal()
        customDriverOptions = ThreadLocal()
        customDriverOptions.set(HashMap())
        appConfiguration = ThreadLocal()
        wrappedDriver = ThreadLocal<WindowsDriver<WebElement>>()
        disposed.set(false)
    }
}