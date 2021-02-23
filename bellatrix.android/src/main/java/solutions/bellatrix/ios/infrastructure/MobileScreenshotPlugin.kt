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

import solutions.bellatrix.ios.infrastructure.DriverService.getWrappedAndroidDriver
import plugins.screenshots.ScreenshotPlugin
import org.apache.commons.io.FileUtils
import java.io.File
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.OutputType
import java.nio.file.Paths
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.ios.configuration.AndroidSettings
import java.util.UUID

class MobileScreenshotPlugin(isEnabled: Boolean?) : ScreenshotPlugin(isEnabled!!) {
    override fun takeScreenshot(screenshotSaveDir: String, filename: String) {
        val screenshot = (getWrappedAndroidDriver() as TakesScreenshot?)!!.getScreenshotAs(OutputType.FILE)
        val destFile = File(Paths.get(screenshotSaveDir, filename).toString() + ".png")
        FileUtils.copyFile(screenshot, destFile)
    }

    override val outputFolder: String
        protected get() {
            var saveLocation: String = ConfigurationService.get<AndroidSettings>().screenshotsSaveLocation
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
        fun of(): MobileScreenshotPlugin {
            val isEnabled: Boolean = ConfigurationService.get<AndroidSettings>().screenshotsOnFailEnabled
            return MobileScreenshotPlugin(isEnabled)
        }
    }
}