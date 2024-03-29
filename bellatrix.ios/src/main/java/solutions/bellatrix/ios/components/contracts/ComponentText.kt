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
package solutions.bellatrix.ios.components.contracts

import solutions.bellatrix.ios.components.IOSComponent
import solutions.bellatrix.ios.validations.ComponentValidator

interface ComponentText : Component {
    val text: String

    fun validateTextIs(value: String) {
        defaultValidateAttributeIs(this as IOSComponent, text, value, "inner text")
    }

    fun validateTextContains(value: String) {
        defaultValidateAttributeContains(this as IOSComponent, text, value, "inner text")
    }

    fun validateTextNotContains(value: String) {
        defaultValidateAttributeNotContains(this as IOSComponent, text, value, "inner text")
    }

    companion object : ComponentValidator()
}