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
import solutions.bellatrix.desktop.components.contracts.ComponentDisabled
import solutions.bellatrix.desktop.components.contracts.ComponentTime

class TimeInput : DesktopComponent(), ComponentDisabled, ComponentTime {
    override val componentClass: Class<*>
        get() = javaClass

    fun setTime(hours: Int, minutes: Int) {
        defaultSetText(SETTING_TIME, TIME_SET, "$hours:$minutes:00")
    }

    override fun getTime(): String {
        return defaultGetText()
    }

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    companion object {
        val SETTING_TIME = EventListener<ComponentActionEventArgs>()
        val TIME_SET = EventListener<ComponentActionEventArgs>()
    }
}