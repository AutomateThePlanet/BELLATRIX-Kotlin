/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Teodor Nikolov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package solutions.bellatrix.web.components.contracts

import solutions.bellatrix.web.components.WebComponent
import solutions.bellatrix.web.validations.ComponentValidator
import java.time.LocalDateTime

interface ComponentDateTime : Component {
    fun getTime(): String
    fun setTime(time: LocalDateTime)

    fun validateTimeIs(value: String) {
        defaultValidateAttributeIs(this as WebComponent, getTime(), value, "time")
    }

    fun validateTimeContains(value: String) {
        defaultValidateAttributeContains(this as WebComponent, getTime(), value, "time")
    }

    fun validateTimeNotContains(value: String) {
        defaultValidateAttributeNotContains(this as WebComponent, getTime(), value, "time")
    }

    companion object : ComponentValidator()
}