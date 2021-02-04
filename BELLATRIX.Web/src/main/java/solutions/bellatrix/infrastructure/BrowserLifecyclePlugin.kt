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

import java.lang.ThreadLocal
import org.testng.ITestResult
import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import solutions.bellatrix.plugins.Plugin
import solutions.bellatrix.plugins.TestResult
import java.lang.Exception
import java.lang.reflect.Method

class BrowserLifecyclePlugin : Plugin() {
    companion object {
        private var currentBrowserConfiguration: ThreadLocal<BrowserConfiguration>
        private var previousBrowserConfiguration: ThreadLocal<BrowserConfiguration>
        private var isBrowserStartedDuringPreBeforeClass: ThreadLocal<Boolean>
        private var isBrowserStartedCorrectly: ThreadLocal<Boolean>

        init {
            currentBrowserConfiguration = ThreadLocal()
            previousBrowserConfiguration = ThreadLocal()
            isBrowserStartedDuringPreBeforeClass = ThreadLocal()
            isBrowserStartedCorrectly = ThreadLocal()
        }

        fun of(): BrowserLifecyclePlugin {
            return BrowserLifecyclePlugin()
        }
    }

    override fun preBeforeClass(type: Class<*>) {
        currentBrowserConfiguration.set(getExecutionBrowserClassLevel(type))
        if (shouldRestartBrowser()) {
            restartBrowser()
            // TODO: maybe we can simplify and remove this parameter.
            isBrowserStartedDuringPreBeforeClass.set(true)
        } else {
            isBrowserStartedDuringPreBeforeClass.set(false)
        }
        super.preBeforeClass(type)
    }

    override fun postAfterClass(type: Class<*>) {
        shutdownBrowser()
        isBrowserStartedDuringPreBeforeClass.set(false)
        super.preAfterClass(type)
    }

    override fun preBeforeTest(testResult: TestResult, memberInfo: Method) {
        currentBrowserConfiguration.set(getBrowserConfiguration(memberInfo))
        if (!isBrowserStartedDuringPreBeforeClass.get() && shouldRestartBrowser()) {
            restartBrowser()
        }

        isBrowserStartedDuringPreBeforeClass.set(false)
    }

    override fun postAfterTest(testResult: TestResult, memberInfo: Method) {
        if (currentBrowserConfiguration.get()!!.lifecycle === Lifecycle.RESTART_ON_FAIL
                && testResult == TestResult.FAILURE) {
            shutdownBrowser()
            isBrowserStartedDuringPreBeforeClass.set(false)
        }
    }

    private fun shutdownBrowser() {
        DriverService.close()
        previousBrowserConfiguration.set(null)
    }

    private fun restartBrowser() {
        shutdownBrowser()
        try {
            DriverService.start(currentBrowserConfiguration.get())
            isBrowserStartedCorrectly.set(true)
        } catch (ex: Exception) {
            isBrowserStartedCorrectly.set(false)
        }
        previousBrowserConfiguration.set(currentBrowserConfiguration.get())
    }

    private fun shouldRestartBrowser(): Boolean {
        // TODO: IsBrowserStartedCorrectly getter?
        val previousConfiguration = previousBrowserConfiguration.get()
        val currentConfiguration = currentBrowserConfiguration.get()
        return if (previousConfiguration == null) {
            true
        } else if (!isBrowserStartedCorrectly.get()) {
            true
        } else if (previousConfiguration != currentConfiguration) {
            true
        } else currentConfiguration.lifecycle === Lifecycle.RESTART_EVERY_TIME
    }

    private fun getBrowserConfiguration(memberInfo: Method): BrowserConfiguration? {
        val classBrowserType = getExecutionBrowserClassLevel(memberInfo.declaringClass)
        val methodBrowserType = getExecutionBrowserMethodLevel(memberInfo)
        val result: BrowserConfiguration? = if (methodBrowserType != null) {
            methodBrowserType
        } else {
            classBrowserType
        }
        return result
    }

    private fun getExecutionBrowserMethodLevel(memberInfo: Method): BrowserConfiguration? {
        val executionBrowserAnnotation = memberInfo.getDeclaredAnnotation(ExecutionBrowser::class.java) as ExecutionBrowser
        return BrowserConfiguration(executionBrowserAnnotation.browser, executionBrowserAnnotation.lifecycle, executionBrowserAnnotation.executionType)
    }

    private fun getExecutionBrowserClassLevel(type: Class<*>): BrowserConfiguration {
        val executionBrowserAnnotation = type.getDeclaredAnnotation(ExecutionBrowser::class.java) as ExecutionBrowser
        return BrowserConfiguration(executionBrowserAnnotation.browser, executionBrowserAnnotation.lifecycle, executionBrowserAnnotation.executionType)
    }
}