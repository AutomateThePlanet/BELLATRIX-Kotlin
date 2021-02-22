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
package solutions.bellatrix.web.components

import solutions.bellatrix.core.utilities.InstanceFactory
import org.openqa.selenium.support.ui.Select

class Select : WebComponent() {
    fun selected(): Option {
        val nativeSelect = Select(findElement())
        val optionComponent: Option = InstanceFactory.create()
        optionComponent.findStrategy = findStrategy
        optionComponent.elementIndex = 0
        optionComponent.wrappedElement = nativeSelect.firstSelectedOption
        return optionComponent
    }

    val options: List<Option>
        get() {
            val nativeSelect = Select(findElement())
            val options = mutableListOf<Option>()
            for (nativeOption in nativeSelect.options) {
                val optionComponent: Option = InstanceFactory.create()
                optionComponent.findStrategy = findStrategy
                optionComponent.elementIndex = 0
                optionComponent.wrappedElement = nativeOption
                options.add(optionComponent)
            }
            return options
        }

    fun selectByText(text: String) {
        Select(findElement()).selectByVisibleText(text)
    }

    fun selectByIndex(index: Int) {
        Select(findElement()).selectByIndex(index)
    }

    override val componentClass: Class<*>
        get() = javaClass
}