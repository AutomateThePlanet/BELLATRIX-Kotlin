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
package solutions.bellatrix.ios.services

import solutions.bellatrix.core.utilities.RuntimeInformation
import java.time.Duration

object AppService : MobileService() {
    var context: String
        get() = wrappedIOSDriver.getContext()
        set(name) {
            wrappedIOSDriver.context(name)
        }

    fun backgroundApp(seconds: Int) {
        wrappedIOSDriver.runAppInBackground(Duration.ofSeconds(seconds.toLong()))
    }

    fun closeApp() {
        wrappedIOSDriver.closeApp()
    }

    fun launchApp() {
        wrappedIOSDriver.launchApp()
    }

    fun resetApp() {
        wrappedIOSDriver.resetApp()
    }

    fun installApp(appPath: String) {
        var appPath = appPath
        if (RuntimeInformation.IS_MAC) {
            appPath = appPath.replace('\\', '/')
        }
        wrappedIOSDriver.installApp(appPath)
    }

    fun removeApp(appId: String) {
        wrappedIOSDriver.removeApp(appId)
    }

    fun isAppInstalled(bundleId: String): Boolean {
        return try {
            wrappedIOSDriver.isAppInstalled(bundleId)
        } catch (e: Exception) {
            false
        }
    }
}