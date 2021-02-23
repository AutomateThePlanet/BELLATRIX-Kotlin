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
package solutions.bellatrix.android.services

import io.appium.java_client.android.Activity
import solutions.bellatrix.core.utilities.RuntimeInformation
import java.lang.Exception
import java.time.Duration

object AppService : MobileService() {
    val currentActivity: String
        get() = wrappedAndroidDriver.currentActivity()

    fun StartActivity(
            appPackage: String,
            appActivity: String,
            appWaitPackage: String,
            appWaitActivity: String,
            stopApp: Boolean) {
        try {
            wrappedAndroidDriver.hideKeyboard()
        } catch (ignore: Exception) {
        }
        val activity = Activity(appPackage, appActivity)
        activity.appWaitActivity = appWaitActivity
        activity.appWaitPackage = appWaitPackage
        activity.isStopApp = stopApp
        wrappedAndroidDriver.startActivity(activity)
    }

    fun startActivityWithIntent(appPackage: String, appActivity: String, intentAction: String, appWaitPackage: String, appWaitActivity: String, intentCategory: String, intentFlags: String, intentOptionalArgs: String, stopApp: Boolean) {
        try {
            wrappedAndroidDriver.hideKeyboard()
        } catch (ignore: Exception) {
        }
        val activity = Activity(appPackage, appActivity)
        activity.appWaitActivity = appWaitActivity
        activity.appWaitPackage = appWaitPackage
        activity.isStopApp = stopApp
        activity.intentCategory = intentCategory
        activity.intentFlags = intentFlags
        activity.optionalIntentArguments = intentOptionalArgs
        wrappedAndroidDriver.startActivity(activity)
    }

    var context: String
        get() = wrappedAndroidDriver.getContext()
        set(name) {
            wrappedAndroidDriver.context(name)
        }

    fun backgroundApp(seconds: Int) {
        wrappedAndroidDriver.runAppInBackground(Duration.ofSeconds(seconds.toLong()))
    }

    fun closeApp() {
        wrappedAndroidDriver.closeApp()
    }

    fun launchApp() {
        wrappedAndroidDriver.launchApp()
    }

    fun resetApp() {
        wrappedAndroidDriver.resetApp()
    }

    fun installApp(appPath: String) {
        var appPath = appPath
        if (RuntimeInformation.IS_MAC) {
            appPath = appPath.replace('\\', '/')
        }
        wrappedAndroidDriver.installApp(appPath)
    }

    fun removeApp(appId: String) {
        wrappedAndroidDriver.removeApp(appId)
    }

    fun isAppInstalled(bundleId: String): Boolean {
        return try {
            wrappedAndroidDriver.isAppInstalled(bundleId)
        } catch (e: Exception) {
            false
        }
    }
}