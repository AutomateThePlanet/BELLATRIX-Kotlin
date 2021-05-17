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

import org.openqa.selenium.support.ui.Select
import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.web.components.contracts.ComponentDisabled
import solutions.bellatrix.web.components.contracts.ComponentReadonly
import solutions.bellatrix.web.components.contracts.ComponentRequired

open class Select : WebComponent(), ComponentDisabled, ComponentRequired, ComponentReadonly {
    override val componentClass: Class<*>
        get() = javaClass

    fun selected(): Option {
        val nativeSelect = Select(findElement())
        val optionComponent: Option = InstanceFactory.create()
        optionComponent.findStrategy = findStrategy
        optionComponent.elementIndex = 0
        wrappedElementHolder = nativeSelect.firstSelectedOption
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
                wrappedElementHolder = nativeOption
                options.add(optionComponent)
            }
            return options
        }

    fun selectByText(text: String) {
        defaultSelectByText(SELECTING, SELECTED, text)
    }

    fun selectByIndex(index: Int) {
        defaultSelectByIndex(SELECTING, SELECTED, index)
    }

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val isRequired: Boolean
        get() = defaultGetRequiredAttribute()

    override val isReadonly: Boolean
        get() = defaultGetReadonlyAttribute()

    companion object {
        val SELECTING = EventListener<ComponentActionEventArgs>()
        val SELECTED = EventListener<ComponentActionEventArgs>()
    }
}