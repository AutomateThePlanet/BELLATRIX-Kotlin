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
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

class NameFindStrategy(value: String) : FindStrategy(value) {
    override fun findElement(driver: WindowsDriver<WebElement>): WebElement {
        return driver.findElementByName(value)
    }

    override fun findAllElements(driver: WindowsDriver<WebElement>): List<WebElement> {
        return driver.findElementsByName(value)
    }

    override fun findElement(element: WebElement): WebElement {
        return element.findElement(By.name(value))
    }

    override fun findAllElements(element: WebElement): List<WebElement> {
        return element.findElements(By.name(value))
    }

    override fun toString(): String {
        return String.format("name = %s", value)
    }
}