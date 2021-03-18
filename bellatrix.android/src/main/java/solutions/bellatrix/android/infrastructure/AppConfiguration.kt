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

import org.jsoup.internal.StringUtil
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.ios.configuration.AndroidSettings
import java.util.*

class AppConfiguration {
    lateinit var appPath: String
    lateinit var lifecycle: Lifecycle
    lateinit var deviceName: String
    lateinit var appPackage: String
    lateinit var appActivity: String
    lateinit var androidVersion: String
    var mobileWebExecution: Boolean = false
    var appiumOptions: HashMap<String, String> = hashMapOf()

    constructor(isMobileWebExecution: Boolean) {
        mobileWebExecution = isMobileWebExecution
    }

    constructor(lifecycle: Lifecycle, androidVersion: String, deviceName: String, appPath: String, appPackage: String, appActivity: String) {
        if (StringUtil.isBlank(androidVersion)) {
            this.androidVersion = ConfigurationService.get<AndroidSettings>().defaultAndroidVersion
        } else {
            this.androidVersion = androidVersion
        }
        if (StringUtil.isBlank(deviceName)) {
            this.deviceName = ConfigurationService.get<AndroidSettings>().defaultDeviceName
        } else {
            this.deviceName = deviceName
        }
        if (StringUtil.isBlank(appPath)) {
            this.appPath = ConfigurationService.get<AndroidSettings>().defaultAppPath
        } else {
            this.appPath = appPath
        }
        this.lifecycle = lifecycle
        if (StringUtil.isBlank(appPackage)) {
            this.appPackage = ConfigurationService.get<AndroidSettings>().defaultAppPackage
        } else {
            this.appPackage = appPackage
        }
        if (StringUtil.isBlank(appActivity)) {
            this.appActivity = ConfigurationService.get<AndroidSettings>().defaultAppActivity
        } else {
            this.appActivity = appActivity
        }
        appiumOptions = HashMap()
    }
}