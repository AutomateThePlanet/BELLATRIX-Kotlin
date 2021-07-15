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
            Anchor.CLICKING.addListener { Log.info("clicking ${it.component.componentName}}") }
            Button.CLICKING.addListener { Log.info("clicking ${it.component.componentName}}") }
            CheckBox.CHECKING.addListener { Log.info("checking ${it.component.componentName}}") }
            CheckBox.UNCHECKING.addListener { Log.info("unchecking ${it.component.componentName}}") }
            ColorInput.SETTING_COLOR.addListener { Log.info("setting '${it.actionValue}' in ${it.component.componentName}}") }
            DateField.SETTING_DATE.addListener { Log.info("setting '${it.actionValue}' in ${it.component.componentName}}") }
            DateTimeField.SETTING_TIME.addListener { Log.info("setting '${it.actionValue}' in ${it.component.componentName}}") }
            EmailField.SETTING_EMAIL.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            FileInput.UPLOADING.addListener { Log.info("uploading '${it.actionValue}' to ${it.component.componentName}}") }
            MonthInput.SETTING_MONTH.addListener { Log.info("setting '${it.actionValue}' in ${it.component.componentName}}") }
            NumberInput.SETTING_NUMBER.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            PasswordField.SETTING_PASSWORD.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            PhoneField.SETTING_PHONE.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            RadioButton.CLICKING.addListener { Log.info("clicking ${it.component.componentName}}") }
            Range.SETTING_RANGE.addListener { Log.info("setting '${it.actionValue}' in ${it.component.componentName}}") }
            Reset.CLICKING.addListener { Log.info("clicking ${it.component.componentName}}") }
            SearchField.SETTING_SEARCH.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            Select.SELECTING.addListener { Log.info("selecting '${it.actionValue}' from ${it.component.componentName}}") }
            TextArea.SETTING_TEXT.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            TextField.SETTING_TEXT.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            TimeInput.SETTING_TIME.addListener { Log.info("setting '${it.actionValue}' in ${it.component.componentName}}") }
            UrlField.SETTING_URL.addListener { Log.info("typing '${it.actionValue}' in ${it.component.componentName}}") }
            WeekInput.SETTING_WEEK.addListener { Log.info("setting '${it.actionValue}' in ${it.component.componentName}}") }
            WebComponent.HOVERING.addListener { Log.info("hovering ${it.component.componentName}}") }
            WebComponent.FOCUSING.addListener { Log.info("focusing ${it.component.componentName}}") }
            WebComponent.SCROLLING_TO_VISIBLE.addListener { Log.info("scrolling to ${it.component.componentName}}") }
            WebComponent.SETTING_ATTRIBUTE.addListener { Log.info("setting ${it.actionValue} to '${it.message}' in ${it.component.componentName}") }
            ComponentValidator.VALIDATING_ATTRIBUTE.addListener { Log.info(it.message) }
            isBddLoggingTurnedOn = true
        }
    }
}