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

import plugins.video.VideoPlugin
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.core.utilities.UserHomePathNormalizer.normalizePath
import solutions.bellatrix.ios.configuration.AndroidSettings
import java.io.File
import java.util.*

class MobileVideoPlugin(isEnabled: Boolean?) : VideoPlugin(isEnabled!!) {
    override val outputFolder: String
        protected get() {
            var saveLocation: String = ConfigurationService.get<AndroidSettings>().videosSaveLocation
            saveLocation = normalizePath(saveLocation)
            val directory = File(saveLocation)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            return saveLocation
        }

    override fun getUniqueFileName(testName: String): String {
        return testName + UUID.randomUUID().toString()
    }

    companion object {
        fun of(): MobileVideoPlugin {
            val isEnabled: Boolean = ConfigurationService.get<AndroidSettings>().videosOnFailEnabled
            return MobileVideoPlugin(isEnabled)
        }
    }
}