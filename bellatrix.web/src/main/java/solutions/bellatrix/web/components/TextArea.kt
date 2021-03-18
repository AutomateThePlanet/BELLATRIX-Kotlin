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

open class TextArea : WebComponent(), ComponentText, ComponentValue, ComponentDisabled, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxLength, ComponentMinLength, ComponentRows, ComponentCols, ComponentPlaceholder, ComponentSpellCheck, ComponentWrap {
    override val componentClass: Class<*>
        get() = javaClass

    @JvmName("getTextFromTextArea")
    fun getText() : String {
        val text = defaultGetText()

        if(text.isEmpty()) {
            return defaultGetValue()
        }

        return text
    }

    fun setText(file: String) {
        this.defaultSetText(SETTING_TEXT, TEXT_SET, file)
    }

    override val text: String
        get() = defaultGetText()

    override val value: String
        get() = defaultGetValue()

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val isAutoComplete: Boolean
        get() = defaultGetAutoCompleteAttribute()

    override val isReadonly: Boolean
        get() = defaultGetReadonlyAttribute()

    override val isRequired: Boolean
        get() = defaultGetRequiredAttribute()

    override val minLength: Int?
        get() = defaultGetMinLengthAttribute()

    override val maxLength: Int?
        get() = defaultGetMaxLengthAttribute()

    override val rows: Int?
        get() = defaultGetRowsAttribute()

    override val cols: Int?
        get() = defaultGetColsAttribute()

    override val placeholder: String
        get() = defaultGetPlaceholderAttribute()

    override val spellcheck: Boolean
        get() = defaultGetSpellCheckAttribute()

    override val wrap: String
        get() = defaultGetWrapAttribute()

    companion object {
        val SETTING_TEXT = EventListener<ComponentActionEventArgs>()
        val TEXT_SET = EventListener<ComponentActionEventArgs>()
    }
}