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
package solutions.bellatrix.web.components.contracts

import solutions.bellatrix.web.components.WebComponent
import solutions.bellatrix.web.validations.ComponentValidator
import java.lang.reflect.InvocationTargetException

interface ComponentTarget : Component {
    val target: String

    fun validateTargetIs(value: String) {
        try {
            defaultValidateAttributeIs(this as WebComponent, target, value, "target")
        } catch (e: InvocationTargetException) {
            throw e.cause!!
        }
    }

    fun validateTargetIsSet() {
        try {
            defaultValidateAttributeIsSet(this as WebComponent, target, "target")
        } catch (e: InvocationTargetException) {
            throw e.cause!!
        }
    }

    fun validateTargetNotSet() {
        try {
            defaultValidateAttributeNotSet(this as WebComponent, target, "target")
        } catch (e: InvocationTargetException) {
            throw e.cause!!
        }
    }

    companion object : ComponentValidator()
}