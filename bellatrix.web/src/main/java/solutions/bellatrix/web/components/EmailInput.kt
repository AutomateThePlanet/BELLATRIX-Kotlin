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

open class EmailInput : WebComponent(), ComponentDisabled, ComponentValue, ComponentEmail, ComponentAutoComplete, ComponentReadonly, ComponentRequired, ComponentMaxLength, ComponentMinLength, ComponentSize, ComponentPlaceholder {
    override val componentClass: Class<*>
        get() = javaClass

    override fun getEmail(): String {
        return value
    }

    override fun setEmail(email: String) {
        setValue(SETTING_EMAIL, EMAIL_SET, email)
    }

    override val isAutoComplete: Boolean
        get() = defaultGetAutoCompleteAttribute()

    override val isDisabled: Boolean
        get() = defaultGetDisabledAttribute()

    override val maxLength: Int?
        get() = defaultGetMaxLengthAttribute()

    override val minLength: Int?
        get() = defaultGetMinLengthAttribute()

    override val placeholder: String
        get() = defaultGetPlaceholderAttribute()

    override val isReadonly: Boolean
        get() = defaultGetReadonlyAttribute()

    override val isRequired: Boolean
        get() = defaultGetRequiredAttribute()

    override val sizeAttribute: Int?
        get() = defaultGetSizeAttribute()

    override val value: String
        get() = defaultGetValue()

    companion object {
        val SETTING_EMAIL = EventListener<ComponentActionEventArgs>()
        val EMAIL_SET = EventListener<ComponentActionEventArgs>()
    }
}