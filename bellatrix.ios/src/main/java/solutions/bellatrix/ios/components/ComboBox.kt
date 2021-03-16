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
package solutions.bellatrix.ios.components

import solutions.bellatrix.ios.components.contracts.ComponentDisabled
import solutions.bellatrix.ios.components.contracts.ComponentText
import solutions.bellatrix.ios.findstrategies.ClassFindStrategy
import solutions.bellatrix.ios.findstrategies.ValueContainingFindStrategy
import solutions.bellatrix.core.plugins.EventListener

class ComboBox : IOSComponent(), ComponentDisabled, ComponentText {
    override val componentClass: Class<*>
        get() = javaClass

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val text: String
        get() {
            var result = defaultGetText()
            if (result.isEmpty()) {
                val textField = create<TextField, ClassFindStrategy>("XCUIElementTypeTextView")
                result = textField.text
            }

            return result
        }

    fun selectByText(value: String) {
        SELECTING.broadcast(ComponentActionEventArgs(this))
        if (findElement().text != value) {
            findElement().click()
            val innerElementToClick = create<RadioButton, ValueContainingFindStrategy>(value)
            innerElementToClick.click()
        }
        SELECTED.broadcast(ComponentActionEventArgs(this))
    }

    companion object {
        val SELECTING = EventListener<ComponentActionEventArgs>()
        val SELECTED = EventListener<ComponentActionEventArgs>()
    }
}