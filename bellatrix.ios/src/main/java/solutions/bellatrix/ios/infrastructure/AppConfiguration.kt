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

import org.jsoup.internal.StringUtil
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.ios.configuration.IOSSettings
import java.util.*

class AppConfiguration {
    lateinit var appPath: String
    lateinit var lifecycle: Lifecycle
    lateinit var deviceName: String
    lateinit var iosVersion: String
    var mobileWebExecution: Boolean = false
    var appiumOptions: HashMap<String, String> = hashMapOf()

    constructor(isMobileWebExecution: Boolean) {
        mobileWebExecution = isMobileWebExecution
    }

    constructor(lifecycle: Lifecycle, androidVersion: String, deviceName: String, appPath: String) {
        if (StringUtil.isBlank(androidVersion)) {
            this.iosVersion = ConfigurationService.get<IOSSettings>().defaultIosVersion
        } else {
            this.iosVersion = androidVersion
        }
        if (StringUtil.isBlank(deviceName)) {
            this.deviceName = ConfigurationService.get<IOSSettings>().defaultDeviceName
        } else {
            this.deviceName = deviceName
        }
        if (StringUtil.isBlank(appPath)) {
            this.appPath = ConfigurationService.get<IOSSettings>().defaultAppPath
        } else {
            this.appPath = appPath
        }
        this.lifecycle = lifecycle
        this.mobileWebExecution = false

        appiumOptions = HashMap()
    }
}