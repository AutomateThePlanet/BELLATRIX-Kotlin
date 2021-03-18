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
import solutions.bellatrix.ios.services.TouchActionsService
import solutions.bellatrix.core.plugins.EventListener

class SeekBar : IOSComponent(), ComponentDisabled {
    override val componentClass: Class<*>
        get() = javaClass

    fun set(percentage: Number) {
        SETTING_PERCENTAGE.broadcast(ComponentActionEventArgs(this, percentage.toString()))
        val end = findElement().size.getWidth()
        val y = findElement().location.getY()
        val touchActionsService = TouchActionsService
        val moveTo = (percentage as Double / 100 * end).toInt()
        touchActionsService.press(moveTo, y).release().perform()
        PERCENTAGE_SET.broadcast(ComponentActionEventArgs(this, percentage.toString()))
    }

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    companion object {
        val SETTING_PERCENTAGE = EventListener<ComponentActionEventArgs>()
        val PERCENTAGE_SET = EventListener<ComponentActionEventArgs>()
    }
}