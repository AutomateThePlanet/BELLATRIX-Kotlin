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

open class Number : WebComponent(), ComponentDisabled, ComponentValue, ComponentNumber, ComponentAutoComplete, ComponentRequired, ComponentReadonly, ComponentPlaceholder, ComponentMax, ComponentMin, ComponentStep {
    override val componentClass: Class<*>
        get() = javaClass

    override fun getNumber(): Double {
        return value.toDouble()
    }

    override fun setNumber(value: Double) {
        setValue(SETTING_NUMBER, NUMBER_SET, value.toString())
    }

    override val isAutoComplete: Boolean
        get() = defaultGetAutoCompleteAttribute()

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val max: Int?
        get() = defaultGetMaxAttribute()

    override val min: Int?
        get() = defaultGetMinAttribute()

    override val placeholder: String
        get() = defaultGetPlaceholderAttribute()

    override val isReadonly: Boolean
        get() = defaultGetReadonlyAttribute()

    override val isRequired: Boolean
        get() = defaultGetRequiredAttribute()

    override val step: Int
        get() = defaultGetStepAttribute()

    override val value: String
        get() = defaultGetValue()

    companion object {
        val SETTING_NUMBER = EventListener<ComponentActionEventArgs>()
        val NUMBER_SET = EventListener<ComponentActionEventArgs>()
    }
}