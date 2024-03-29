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
package solutions.bellatrix.android.findstrategies

import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver

class IdFindStrategy(value: String) : FindStrategy(value) {
    private val CLASS_EXPRESSION = "new UiScrollable(new UiSelector()).scrollIntoView(new UiSelector().resourceId(\\\"%s\\\"));"
    override fun findElement(driver: AndroidDriver<MobileElement>): MobileElement {
        return driver.findElementByAndroidUIAutomator(String.format(CLASS_EXPRESSION, value))
    }

    override fun findAllElements(driver: AndroidDriver<MobileElement>): List<MobileElement> {
        return driver.findElementsByAndroidUIAutomator(String.format(CLASS_EXPRESSION, value))
    }

    override fun findElement(element: MobileElement): MobileElement {
        throw KotlinReflectionNotSupportedError();
    }

    override fun findAllElements(element: MobileElement): List<MobileElement> {
        throw KotlinReflectionNotSupportedError();
    }

    override fun toString(): String {
        return String.format("text = %s", value)
    }
}