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
package solutions.bellatrix.desktop.components

import solutions.bellatrix.core.plugins.EventListener
import solutions.bellatrix.desktop.components.contracts.ComponentDate
import solutions.bellatrix.desktop.components.contracts.ComponentDisabled

class Date : DesktopComponent(), ComponentDisabled, ComponentDate {
    override val componentClass: Class<*>
        get() = javaClass

    fun setDate(year: Int, month: Int, day: Int) {
        if (year <= 0) throw IllegalArgumentException("The year should be a positive number but you specified: $year")
        if (month <= 0 || month > 12) throw IllegalArgumentException("The month should be between 0 and 12 but you specified: $month")
        if (day <= 0 || day > 31) throw IllegalArgumentException("The day should be between 0 and 31 but you specified: $day")

        var valueToBeSet = if (month < 10) "0$month\\$year" else "0$month\\$year"
        valueToBeSet = if (day < 10) "$valueToBeSet-0$day" else "$valueToBeSet-$day"

        defaultSetText(SETTING_DATE, DATE_SET, valueToBeSet)
    }

    override fun getDate(): String {
        return defaultGetText()
    }

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    companion object {
        val SETTING_DATE = EventListener<ComponentActionEventArgs>()
        val DATE_SET = EventListener<ComponentActionEventArgs>()
    }
}