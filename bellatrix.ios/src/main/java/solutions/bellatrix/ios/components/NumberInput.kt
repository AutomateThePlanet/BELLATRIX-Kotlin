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
import solutions.bellatrix.ios.components.contracts.ComponentNumber
import solutions.bellatrix.ios.findstrategies.ClassFindStrategy
import solutions.bellatrix.core.plugins.EventListener

class NumberInput : IOSComponent(), ComponentDisabled, ComponentNumber {
    override val componentClass: Class<*>
        get() = javaClass

    override fun getNumber(): Double {
        val resultText = defaultGetText()
        return resultText.toDouble()
    }

    fun setNumber(value: Number) {
        defaultSetText(SETTING_NUMBER, NUMBER_SET, value.toString())
    }

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    companion object {
        val SETTING_NUMBER = EventListener<ComponentActionEventArgs>()
        val NUMBER_SET = EventListener<ComponentActionEventArgs>()
    }
}