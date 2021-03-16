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
package solutions.bellatrix.ios.infrastructure

import io.appium.java_client.MobileElement
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.MobileCapabilityType
import org.openqa.selenium.MutableCapabilities
import org.openqa.selenium.Platform
import org.openqa.selenium.remote.DesiredCapabilities
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.ios.configuration.GridSettings
import solutions.bellatrix.ios.configuration.IOSSettings
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

object DriverService {
    private var disposed: ThreadLocal<Boolean> = ThreadLocal()
    private var appConfiguration: ThreadLocal<AppConfiguration> = ThreadLocal()
    private var customDriverOptions: ThreadLocal<HashMap<String, String>> = ThreadLocal()
    private var wrappedIOSDriver: ThreadLocal<IOSDriver<MobileElement>> = ThreadLocal()
    fun getCustomDriverOptions(): HashMap<String, String> {
        return customDriverOptions.get()
    }

    fun addDriverOptions(key: String, value: String) {
        customDriverOptions.get()[key] = value
    }

    fun getWrappedIOSDriver(): IOSDriver<MobileElement> {
        return wrappedIOSDriver.get()
    }

    fun getAppConfiguration(): AppConfiguration {
        return appConfiguration.get()
    }

    fun start(configuration: AppConfiguration): IOSDriver<MobileElement> {
        appConfiguration.set(configuration)
        disposed.set(false)
        val IOSSettings: IOSSettings = ConfigurationService.get<IOSSettings>()
        val executionType: String = IOSSettings.executionType
        var driver: IOSDriver<MobileElement> = if (executionType.equals("regular")) {
            initializeDriverRegularMode(IOSSettings.serviceUrl)
        } else {
            val gridSettings = IOSSettings.gridSettings.stream().filter { g -> g.providerName.equals(executionType.toLowerCase()) }.findFirst()
            initializeDriverGridMode(IOSSettings.gridSettings.stream().filter { g -> g.providerName.equals(executionType.toLowerCase()) }.findFirst().get())
        }
        
        driver.manage().timeouts().implicitlyWait(IOSSettings.timeoutSettings.implicitWaitTimeout, TimeUnit.SECONDS)
        wrappedIOSDriver.set(driver)
        return driver
    }

    private fun initializeDriverGridMode(gridSettings: GridSettings): IOSDriver<MobileElement> {
        val caps = DesiredCapabilities()
        caps.setCapability("platform", Platform.WIN10)
        caps.setCapability("version", "latest")
        var driver: IOSDriver<MobileElement> = try {
            IOSDriver<MobileElement>(URL(gridSettings.url), caps)
        } catch (e: MalformedURLException) {
            e.debugStackTrace()
            IOSDriver<MobileElement>(URL(gridSettings.url), caps)
        }
        return driver
    }

    private fun initializeDriverRegularMode(serviceUrl: String): IOSDriver<MobileElement> {
        val caps = DesiredCapabilities()
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, getAppConfiguration().iosVersion)
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, getAppConfiguration().deviceName)
        if (getAppConfiguration().mobileWebExecution) {
            caps.setCapability(MobileCapabilityType.BROWSER_NAME, ConfigurationService.get<IOSSettings>().defaultBrowser)
        } else {
            caps.setCapability(MobileCapabilityType.APP, getAppConfiguration().appPath)
        }
        addDriverOptions(caps)
        return IOSDriver(URL(serviceUrl), caps)
    }

    private fun <TOption : MutableCapabilities?> addGridOptions(options: TOption, gridSettings: GridSettings) {
        for (entry in gridSettings.arguments) {
            for ((key, value) in entry) {
                if (key.startsWith("env_")) {
                    val envValue = System.getProperty(key.replace("env_", ""))
                    options?.setCapability(key, envValue)
                } else {
                    options?.setCapability(key, value)
                }
            }
        }
    }

    private fun <TOption : MutableCapabilities> addDriverOptions(options: TOption) {
        for (optionKey in appConfiguration.get().appiumOptions.keys) {
            options.setCapability(optionKey, appConfiguration.get().appiumOptions[optionKey])
        }
    }

    fun close() {
        if (disposed.get()) {
            return
        }
        if (wrappedIOSDriver.get() != null) {
            wrappedIOSDriver.get().close()
            customDriverOptions.get().clear()
        }
        disposed.set(true)
    }

    init {
        disposed = ThreadLocal()
        customDriverOptions = ThreadLocal()
        customDriverOptions.set(HashMap())
        appConfiguration = ThreadLocal()
        wrappedIOSDriver = ThreadLocal()
        disposed.set(false)
    }
}