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

import plugins.screenshots.ScreenshotsPlugin
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.shooting.ShootingStrategies
import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import java.io.File
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO

class WebScreenshotsPlugin(isEnabled: Boolean) : ScreenshotsPlugin(isEnabled) {
    protected override fun takeScreenshot(screenshotSaveDir: String, filename: String) {
        val screenshot = AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(DriverService.wrappedDriver())
        val destFile = File(Paths.get(screenshotSaveDir, filename).toString())
        ImageIO.write(screenshot.image, "png", destFile)
    }

    protected override val outputFolder: String
        protected get() {
            var saveLocation: String = ConfigurationService.get<WebSettings>().screenshotsSaveLocation
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

    protected override fun getUniqueFileName(testName: String): String {
        return testName + UUID.randomUUID().toString() + ".png"
    }

    companion object {
        fun of(): WebScreenshotsPlugin {
            val isEnabled: Boolean = ConfigurationService.get<WebSettings>().screenshotsOnFailEnabled
            return WebScreenshotsPlugin(isEnabled)
        }
    }
}