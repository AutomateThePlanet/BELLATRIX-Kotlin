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

import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.plugins.Plugin
import solutions.bellatrix.core.plugins.TestResult
import solutions.bellatrix.core.utilities.UserHomePathNormalizer.normalizePath
import solutions.bellatrix.core.utilities.debugStackTrace
import solutions.bellatrix.desktop.configuration.DesktopSettings
import java.lang.reflect.Method

class AppLifecyclePlugin : Plugin() {
    companion object {
        private var currentAppConfiguration: ThreadLocal<AppConfiguration> = ThreadLocal()
        private var previousAppConfiguration: ThreadLocal<AppConfiguration> = ThreadLocal()
        private var isAppStartedDuringPreBeforeClass: ThreadLocal<Boolean> = ThreadLocal()
        private var isAppStartedCorrectly: ThreadLocal<Boolean> = ThreadLocal()

        fun of(): AppLifecyclePlugin {
            return AppLifecyclePlugin()
        }
    }

    override fun preBeforeClass(type: Class<*>) {
        currentAppConfiguration.set(getExecutionAppClassLevel(type))
        if (shouldRestartApp()) {
            restartApp()
            // TODO: maybe we can simplify and remove this parameter.
            isAppStartedDuringPreBeforeClass.set(true)
        } else {
            isAppStartedDuringPreBeforeClass.set(false)
        }
        super.preBeforeClass(type)
    }

    override fun postAfterClass(type: Class<*>) {
        shutdownApp()
        isAppStartedDuringPreBeforeClass.set(false)
        super.preAfterClass(type)
    }

    override fun preBeforeTest(testResult: TestResult, memberInfo: Method) {
        currentAppConfiguration.set(getAppConfiguration(memberInfo))
        if (!isAppStartedDuringPreBeforeClass.get()) {
            if (shouldRestartApp()) {
                restartApp()
            }
        }
        isAppStartedDuringPreBeforeClass.set(false)
    }

    override fun postAfterTest(testResult: TestResult, memberInfo: Method) {
        if (currentAppConfiguration.get().lifecycle ===
                Lifecycle.RESTART_ON_FAIL && testResult === TestResult.FAILURE) {
            shutdownApp()
            isAppStartedDuringPreBeforeClass.set(false)
        }
    }

    private fun shutdownApp() {
        DriverService.close()
        previousAppConfiguration.set(null)
    }

    private fun restartApp() {
        shutdownApp()
        try {
            DriverService.start(currentAppConfiguration.get())
            isAppStartedCorrectly.set(true)
        } catch (ex: Exception) {
            ex.debugStackTrace()
            isAppStartedCorrectly.set(false)
        }
        previousAppConfiguration.set(currentAppConfiguration.get())
    }

    private fun shouldRestartApp(): Boolean {
        // TODO: IsAppStartedCorrectly getter?
        val previousConfiguration = previousAppConfiguration.get()
        val currentConfiguration = currentAppConfiguration.get()
        return if (previousConfiguration == null) {
            true
        } else if (!isAppStartedCorrectly.get()) {
            true
        } else if (previousConfiguration != currentConfiguration) {
            true
        } else if (currentConfiguration.lifecycle === Lifecycle.RESTART_EVERY_TIME) {
            true
        } else {
            false
        }
    }

    private fun getAppConfiguration(memberInfo: Method): AppConfiguration? {
        var result: AppConfiguration? = null
        val classAppType = getExecutionAppClassLevel(memberInfo.declaringClass)
        val methodAppType = getExecutionAppMethodLevel(memberInfo)
        if (methodAppType != null) {
            result = methodAppType
        } else {
            result = classAppType
        }
        return result
    }

    private fun getExecutionAppMethodLevel(memberInfo: Method): AppConfiguration? {
        val executionAppAnnotation = memberInfo.getDeclaredAnnotation(ExecutionApp::class.java) as ExecutionApp
        return AppConfiguration(executionAppAnnotation.lifecycle, executionAppAnnotation.appPath)
    }

    private fun getExecutionAppClassLevel(type: Class<*>): AppConfiguration {
        val executionAppAnnotation = type.getDeclaredAnnotation(ExecutionApp::class.java) as ExecutionApp
        if (executionAppAnnotation == null) {
            var defaultAppPath: String = ConfigurationService.get<DesktopSettings>().defaultAppPath
            defaultAppPath = normalizePath(defaultAppPath)
            val defaultLifecycle: Lifecycle = Lifecycle.fromText(ConfigurationService.get<DesktopSettings>().defaultLifeCycle)
            return AppConfiguration(defaultLifecycle, defaultAppPath)
        }
        return AppConfiguration(executionAppAnnotation.lifecycle, executionAppAnnotation.appPath)
    }
}