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

import org.apache.commons.io.FileUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import plugins.screenshots.ScreenshotPlugin
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.desktop.configuration.DesktopSettings
import solutions.bellatrix.desktop.infrastructure.DriverService.getWrappedDriver
import java.io.File
import java.nio.file.Paths
import java.util.*

class DesktopScreenshotPlugin(isEnabled: Boolean) : ScreenshotPlugin(isEnabled) {
    override fun takeScreenshot(screenshotSaveDir: String, filename: String) {
        val screenshot = (getWrappedDriver() as TakesScreenshot).getScreenshotAs(OutputType.FILE)
        val destFile = File(Paths.get(screenshotSaveDir, filename).toString() + ".png")
        FileUtils.copyFile(screenshot, destFile)
    }

    override val outputFolder: String
        protected get() {
            var saveLocation: String = ConfigurationService.get<DesktopSettings>().screenshotsSaveLocation
            if (saveLocation.startsWith("user.home")) {
                val userHomeDir = System.getProperty("user.home")
                saveLocation = saveLocation.replace("user.home", userHomeDir)
            }
            val directory = File(saveLocation)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            return saveLocation
        }

    override fun getUniqueFileName(testName: String): String {
        return testName + UUID.randomUUID().toString() + ".png"
    }

    companion object {
        fun of(): DesktopScreenshotPlugin {
            val isEnabled: Boolean = ConfigurationService.get<DesktopSettings>().screenshotsOnFailEnabled
            return DesktopScreenshotPlugin(isEnabled)
        }
    }
}