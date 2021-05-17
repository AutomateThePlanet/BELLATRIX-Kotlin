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

import plugins.screenshots.ScreenshotPlugin
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.shooting.ShootingStrategies
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.UserHomePathNormalizer.normalizePath
import solutions.bellatrix.web.configuration.WebSettings
import java.io.File
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO

class WebScreenshotPlugin(isEnabled: Boolean) : ScreenshotPlugin(isEnabled) {
    override fun takeScreenshot(screenshotSaveDir: String, filename: String) {
        val screenshot = AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(DriverService.wrappedDriver())
        val destFile = File(Paths.get(screenshotSaveDir, filename).toString())
        ImageIO.write(screenshot.image, "png", destFile)
    }

    override val outputFolder: String
        get() {
            var saveLocation: String = ConfigurationService.get<WebSettings>().screenshotsSaveLocation
            saveLocation = normalizePath(saveLocation)
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
        fun of(): WebScreenshotPlugin {
            val isEnabled: Boolean = ConfigurationService.get<WebSettings>().screenshotsOnFailEnabled
            return WebScreenshotPlugin(isEnabled)
        }
    }
}