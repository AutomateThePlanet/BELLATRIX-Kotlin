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

import org.openqa.selenium.Rotatable
import org.openqa.selenium.ScreenOrientation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DeviceService : MobileService() {
    val deviceTime: LocalDateTime
        get() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            return LocalDateTime.parse(wrappedIOSDriver.deviceTime, formatter)
        }
    val screenOrientation: ScreenOrientation
        get() = wrappedIOSDriver.orientation

    fun rotate(newOrientation: ScreenOrientation?) {
        (wrappedIOSDriver as Rotatable).rotate(newOrientation)
    }

    val isLocked: Boolean
        get() = wrappedIOSDriver.isDeviceLocked

    fun lock() {
        wrappedIOSDriver.lockDevice()
    }

    fun unlock() {
        wrappedIOSDriver.unlockDevice()
    }

    fun shake() {
        wrappedIOSDriver.shake()
    }

    fun setSetting(setting: String?, value: Any?) {
        wrappedIOSDriver.setSetting(setting, value)
    }

    val setting: Map<String, Any>
        get() = wrappedIOSDriver.settings
}