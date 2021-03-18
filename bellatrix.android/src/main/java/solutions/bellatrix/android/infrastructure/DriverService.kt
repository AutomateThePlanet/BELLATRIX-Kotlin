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
package solutions.bellatrix.android.infrastructure

import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.remote.AndroidMobileCapabilityType
import io.appium.java_client.remote.MobileCapabilityType
import org.openqa.selenium.MutableCapabilities
import org.openqa.selenium.Platform
import org.openqa.selenium.remote.DesiredCapabilities
import solutions.bellatrix.android.configuration.AndroidSettings
import solutions.bellatrix.android.configuration.GridSettings
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.debugStackTrace
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

object DriverService {
    private var disposed: ThreadLocal<Boolean> = ThreadLocal()
    private var appConfiguration: ThreadLocal<AppConfiguration> = ThreadLocal()
    private var customDriverOptions: ThreadLocal<HashMap<String, String>> = ThreadLocal()
    private var wrappedAndroidDriver: ThreadLocal<AndroidDriver<MobileElement>> = ThreadLocal()
    fun getCustomDriverOptions(): HashMap<String, String> {
        return customDriverOptions.get()
    }

    fun addDriverOptions(key: String, value: String) {
        customDriverOptions.get()[key] = value
    }

    fun getWrappedAndroidDriver(): AndroidDriver<MobileElement> {
        return wrappedAndroidDriver.get()
    }

    fun getAppConfiguration(): AppConfiguration {
        return appConfiguration.get()
    }

    fun start(configuration: AppConfiguration): AndroidDriver<MobileElement> {
        appConfiguration.set(configuration)
        disposed.set(false)
        val androidSettings: AndroidSettings = ConfigurationService.get<AndroidSettings>()
        val executionType: String = androidSettings.executionType
        var driver: AndroidDriver<MobileElement> = if (executionType.equals("regular")) {
            initializeDriverRegularMode(androidSettings.serviceUrl)
        } else {
            val gridSettings = androidSettings.gridSettings.stream().filter { g -> g.providerName.equals(executionType.toLowerCase()) }.findFirst()
            initializeDriverGridMode(androidSettings.gridSettings.stream().filter { g -> g.providerName.equals(executionType.toLowerCase()) }.findFirst().get())
        }
        
        driver.manage().timeouts().implicitlyWait(androidSettings.timeoutSettings.implicitWaitTimeout, TimeUnit.SECONDS)
        wrappedAndroidDriver.set(driver)
        return driver
    }

    private fun initializeDriverGridMode(gridSettings: GridSettings): AndroidDriver<MobileElement> {
        val caps = DesiredCapabilities()
        caps.setCapability("platform", Platform.WIN10)
        caps.setCapability("version", "latest")
        var driver: AndroidDriver<MobileElement> = try {
            AndroidDriver<MobileElement>(URL(gridSettings.url), caps)
        } catch (e: MalformedURLException) {
            e.debugStackTrace()
            AndroidDriver<MobileElement>(URL(gridSettings.url), caps)
        }
        return driver
    }

    private fun initializeDriverRegularMode(serviceUrl: String): AndroidDriver<MobileElement> {
        val caps = DesiredCapabilities()
        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android")
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, getAppConfiguration().androidVersion)
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, getAppConfiguration().deviceName)
        if (getAppConfiguration().mobileWebExecution) {
            caps.setCapability(MobileCapabilityType.BROWSER_NAME, ConfigurationService.get<AndroidSettings>().defaultBrowser)
        } else {
            caps.setCapability(MobileCapabilityType.APP, getAppConfiguration().appPath)
            caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, getAppConfiguration().appPackage)
            caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, getAppConfiguration().appActivity)
        }
        addDriverOptions(caps)
        return AndroidDriver(URL(serviceUrl), caps)
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
        if (wrappedAndroidDriver.get() != null) {
            wrappedAndroidDriver.get().close()
            customDriverOptions.get().clear()
        }
        disposed.set(true)
    }

    init {
        disposed = ThreadLocal()
        customDriverOptions = ThreadLocal()
        customDriverOptions.set(HashMap())
        appConfiguration = ThreadLocal()
        wrappedAndroidDriver = ThreadLocal()
        disposed.set(false)
    }
}