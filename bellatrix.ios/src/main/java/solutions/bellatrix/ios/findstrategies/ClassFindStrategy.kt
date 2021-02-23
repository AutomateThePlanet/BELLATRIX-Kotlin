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
package solutions.bellatrix.ios.findstrategies

import io.appium.java_client.MobileElement
import io.appium.java_client.ios.IOSDriver

class ClassFindStrategy(value: String) : FindStrategy(value) {
    override fun findElement(driver: IOSDriver<MobileElement>): MobileElement {
        return driver.findElementByClassName(value)
    }

    override fun findAllElements(driver: IOSDriver<MobileElement>): List<MobileElement> {
        return driver.findElementsByClassName(value)
    }

    override fun findElement(element: MobileElement): MobileElement {
        return element.findElementByClassName(value)
    }

    override fun findAllElements(element: MobileElement): List<MobileElement> {
        return element.findElementsByClassName(value)
    }

    override fun toString(): String {
        return String.format("class = %s", value)
    }
}