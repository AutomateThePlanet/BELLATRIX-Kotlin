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
package solutions.bellatrix.desktop.findstrategies

import io.appium.java_client.windows.WindowsDriver
import io.appium.java_client.windows.WindowsElement
import io.appium.java_client.MobileElement

class AutomationIdFindStrategy(value: String) : FindStrategy(value) {
    override fun findElement(driver: WindowsDriver<WindowsElement>): WindowsElement {
        return driver.findElementByWindowsUIAutomation(value)
    }

    override fun findAllElements(driver: WindowsDriver<WindowsElement>): List<WindowsElement> {
        return driver.findElementsByWindowsUIAutomation(value)
    }

    override fun findElement(element: WindowsElement): MobileElement {
        return element.findElementByWindowsUIAutomation(value)
    }

    override fun findAllElements(element: WindowsElement): List<MobileElement> {
        return element.findElementsByWindowsUIAutomation(value)
    }

    override fun toString(): String {
        return String.format("automationId %s", value)
    }
}