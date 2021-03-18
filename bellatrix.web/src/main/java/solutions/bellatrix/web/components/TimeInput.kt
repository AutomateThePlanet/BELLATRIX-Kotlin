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

open class TimeInput : WebComponent(), ComponentDisabled, ComponentValue, ComponentTime, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxText, ComponentMinText, ComponentStep {
    override val componentClass: Class<*>
        get() = javaClass

    override fun getTime(): String {
        return value
    }

    override fun setTime(hours: Int, minutes: Int) {
        setValue(SETTING_TIME, TIME_SET, "$hours:$minutes:00")
    }

    override val isAutoComplete: Boolean
        get() = defaultGetAutoCompleteAttribute()

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val max: String
        get() = defaultGetMaxAttributeAsString()

    override val min: String
        get() = defaultGetMinAttributeAsString()

    override val isReadonly: Boolean
        get() = defaultGetReadonlyAttribute()

    override val isRequired: Boolean
        get() = defaultGetRequiredAttribute()

    override val step: Double?
        get() = defaultGetStepAttribute()

    override val value: String
        get() = defaultGetValue()

    companion object {
        val SETTING_TIME = EventListener<ComponentActionEventArgs>()
        val TIME_SET = EventListener<ComponentActionEventArgs>()
    }
}