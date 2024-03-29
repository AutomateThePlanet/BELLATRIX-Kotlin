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

import io.appium.java_client.android.nativekey.AndroidKey
import io.appium.java_client.android.nativekey.KeyEvent
import org.openqa.selenium.WebDriverException

object KeyboardService : MobileService() {
    fun hideKeyboard() {
        try {
            wrappedAndroidDriver.hideKeyboard()
        } catch (ignored: WebDriverException) {
        }
    }

    fun longPressKey(androidKey: AndroidKey?) {
        wrappedAndroidDriver.longPressKey(KeyEvent(androidKey))
    }

    fun pressKey(androidKey: AndroidKey?) {
        wrappedAndroidDriver.pressKey(KeyEvent(androidKey))
    }
}