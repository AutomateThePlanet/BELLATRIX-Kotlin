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
import kotlin.Number

open class Range : WebComponent(), ComponentDisabled, ComponentValue, ComponentRange, ComponentList, ComponentAutoComplete, ComponentRequired, ComponentMax, ComponentMin, ComponentStep {
    override val componentClass: Class<*>
        get() = javaClass

    override fun getRange(): Double {
        return value.toDouble()
    }

    override fun setRange(value: Number) {
        setValue(SETTING_RANGE, RANGE_SET, value.toString())
    }

    override val isAutoComplete: Boolean
        get() = defaultGetAutoCompleteAttribute()

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val list: String
        get() = defaultGetList()

    override val max: Double?
        get() = defaultGetMaxAttribute()

    override val min: Double?
        get() = defaultGetMinAttribute()

    override val isRequired: Boolean
        get() = defaultGetRequiredAttribute()

    override val step: Double?
        get() = defaultGetStepAttribute()

    override val value: String
        get() = defaultGetValue()

    companion object {
        val SETTING_RANGE = EventListener<ComponentActionEventArgs>()
        val RANGE_SET = EventListener<ComponentActionEventArgs>()
    }
}