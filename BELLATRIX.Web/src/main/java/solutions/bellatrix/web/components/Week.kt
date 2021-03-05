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

open class Week : WebComponent(), ComponentDisabled, ComponentValue, ComponentWeek, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxText, ComponentMinText, ComponentStep {
    override val componentClass: Class<*>
        get() = javaClass

    override fun getWeek(): String {
        return value
    }

    override fun setWeek(year: Int, weekNumber: Int) {
        defaultSetWeek(year, weekNumber)
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

    override val step: Int
        get() = defaultGetStepAttribute()

    override val value: String
        get() = defaultGetValue()

    protected fun defaultSetWeek(year: Int, weekNumber: Int) {
        if (weekNumber <= 0 || weekNumber > 52) throw IllegalArgumentException("The week number should be between 0 and 53 but you specified: $weekNumber")
        if (year <= 0) throw IllegalArgumentException("The year should be a positive number but you specified: $year")

        val valueToBeSet = if (weekNumber < 10) "$year-W0$weekNumber" else "$year-W$weekNumber"
        setValue(SETTING_WEEK, WEEK_SET, valueToBeSet)
    }

    companion object {
        val SETTING_WEEK = EventListener<ComponentActionEventArgs>()
        val WEEK_SET = EventListener<ComponentActionEventArgs>()
    }
}