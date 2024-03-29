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

import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.web.components.contracts.*

open class ColorInput : WebComponent(), ComponentDisabled, ComponentValue, ComponentColor, ComponentList, ComponentAutoComplete, ComponentRequired {
    override val componentClass: Class<*>
        get() = javaClass

    override fun getColor(): String {
        return value
    }

    override fun setColor(value: String) {
        setValue(SETTING_COLOR, COLOR_SET, value)
    }

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val isAutoComplete: Boolean
        get() = defaultGetAutoCompleteAttribute()

    override val list: String
        get() = defaultGetList()

    override val isRequired: Boolean
        get() = defaultGetRequiredAttribute()

    override val value: String
        get() = defaultGetValue()

    companion object {
        val SETTING_COLOR = EventListener<ComponentActionEventArgs>()
        val COLOR_SET = EventListener<ComponentActionEventArgs>()
    }
}