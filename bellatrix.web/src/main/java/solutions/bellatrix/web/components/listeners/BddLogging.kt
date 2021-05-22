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
package solutions.bellatrix.web.components.listeners

import solutions.bellatrix.core.plugins.Listener
import solutions.bellatrix.core.utilities.Log
import solutions.bellatrix.web.components.*
import solutions.bellatrix.web.validations.ComponentValidator

object BddLogging : Listener() {
    private var isBddLoggingTurnedOn = false
    override fun addListener() {
        if (!isBddLoggingTurnedOn) {
            Anchor.CLICKING.addListener { Log.info("clicking ${it.component.elementName}}") }
            Button.CLICKING.addListener { Log.info("clicking ${it.component.elementName}}") }
            CheckBox.CHECKING.addListener { Log.info("checking ${it.component.elementName}}") }
            CheckBox.UNCHECKING.addListener { Log.info("unchecking ${it.component.elementName}}") }
            ColorInput.SETTING_COLOR.addListener { Log.info("setting '${it.actionValue}' in ${it.component.elementName}}") }
            DateInput.SETTING_DATE.addListener { Log.info("setting '${it.actionValue}' in ${it.component.elementName}}") }
            DateTimeInput.SETTING_TIME.addListener { Log.info("setting '${it.actionValue}' in ${it.component.elementName}}") }
            EmailInput.SETTING_EMAIL.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            FileInput.UPLOADING.addListener { Log.info("uploading '${it.actionValue}' to ${it.component.elementName}}") }
            MonthInput.SETTING_MONTH.addListener { Log.info("setting '${it.actionValue}' in ${it.component.elementName}}") }
            NumberInput.SETTING_NUMBER.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            PasswordInput.SETTING_PASSWORD.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            PhoneInput.SETTING_PHONE.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            RadioButton.CLICKING.addListener { Log.info("clicking ${it.component.elementName}}") }
            Range.SETTING_RANGE.addListener { Log.info("setting '${it.actionValue}' in ${it.component.elementName}}") }
            Reset.CLICKING.addListener { Log.info("clicking ${it.component.elementName}}") }
            SearchInput.SETTING_SEARCH.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            Select.SELECTING.addListener { Log.info("selecting '${it.actionValue}' from ${it.component.elementName}}") }
            TextArea.SETTING_TEXT.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            TextField.SETTING_TEXT.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            TimeInput.SETTING_TIME.addListener { Log.info("setting '${it.actionValue}' in ${it.component.elementName}}") }
            UrlInput.SETTING_URL.addListener { Log.info("typing '${it.actionValue}' in ${it.component.elementName}}") }
            WeekInput.SETTING_WEEK.addListener { Log.info("setting '${it.actionValue}' in ${it.component.elementName}}") }
            WebComponent.HOVERING.addListener { Log.info("hovering ${it.component.elementName}}") }
            WebComponent.FOCUSING.addListener { Log.info("focusing ${it.component.elementName}}") }
            WebComponent.SCROLLING_TO_VISIBLE.addListener { Log.info("scrolling to ${it.component.elementName}}") }
            WebComponent.SETTING_ATTRIBUTE.addListener { Log.info("setting ${it.actionValue} to '${it.message}' in ${it.component.elementName}") }
            ComponentValidator.VALIDATING_ATTRIBUTE.addListener { Log.info(it.message) }
            isBddLoggingTurnedOn = true
        }
    }
}