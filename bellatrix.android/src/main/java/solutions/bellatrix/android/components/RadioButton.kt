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
package solutions.bellatrix.android.components

import solutions.bellatrix.android.components.contracts.ComponentChecked
import solutions.bellatrix.android.components.contracts.ComponentDisabled
import solutions.bellatrix.android.components.contracts.ComponentText
import solutions.bellatrix.core.plugins.EventListener

class RadioButton : AndroidComponent(), ComponentDisabled, ComponentChecked, ComponentText {
    override val componentClass: Class<*>
        get() = javaClass

    fun click() {
        defaultClick(CLICKING, CLICKED)
    }

    override val isChecked: Boolean
        get() = defaultGetCheckedAttribute()

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val text: String
        get() = defaultGetText()

    companion object {
        val CLICKING = EventListener<ComponentActionEventArgs>()
        val CLICKED = EventListener<ComponentActionEventArgs>()
    }
}