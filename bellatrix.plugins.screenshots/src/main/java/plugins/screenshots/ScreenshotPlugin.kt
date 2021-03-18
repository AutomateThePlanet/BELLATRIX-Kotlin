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
package plugins.screenshots

import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.core.plugins.Plugin
import solutions.bellatrix.core.plugins.TestResult
import java.lang.reflect.Method
import java.nio.file.Paths

abstract class ScreenshotPlugin(private val isEnabled: Boolean) : Plugin() {
    protected abstract fun takeScreenshot(screenshotSaveDir: String, filename: String)
    protected abstract val outputFolder: String
    protected abstract fun getUniqueFileName(testName: String): String

    override fun preAfterTest(testResult: TestResult, memberInfo: Method) {
        if (!isEnabled && testResult == TestResult.SUCCESS)
            return

        try {
            val screenshotSaveDir = outputFolder
            val screenshotFileName = getUniqueFileName(memberInfo.name)
            takeScreenshot(screenshotSaveDir, screenshotFileName)
            SCREENSHOT_GENERATED.broadcast(ScreenshotPluginEventArgs(Paths.get(screenshotSaveDir, screenshotFileName).toString()))
        } catch (e: Exception) {
            // ignore since it is failing often because of bugs in Remote driver for Chrome
            e.printStackTrace()
        }
    }

    companion object {
        val SCREENSHOT_GENERATED = EventListener<ScreenshotPluginEventArgs>()
    }
}