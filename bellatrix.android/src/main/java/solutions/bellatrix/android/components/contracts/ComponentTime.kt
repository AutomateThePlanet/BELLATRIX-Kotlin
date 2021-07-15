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
package solutions.bellatrix.android.components.contracts

import solutions.bellatrix.android.components.AndroidComponent
import solutions.bellatrix.android.validations.ComponentValidator
import java.lang.reflect.InvocationTargetException

interface ComponentTime : Component {
    fun getTime(): String

    fun validateTimeIs(value: String) {
        try {
            defaultValidateAttributeIs(this as AndroidComponent, getTime(), value, "time")
        } catch (e: InvocationTargetException) {
            throw e.cause!!
        }
    }

    fun validateTimeContains(value: String) {
        try {
            defaultValidateAttributeContains(this as AndroidComponent, getTime(), value, "time")
        } catch (e: InvocationTargetException) {
            throw e.cause!!
        }
    }

    fun validateTimeNotContains(value: String) {
        try {
            defaultValidateAttributeNotContains(this as AndroidComponent, getTime(), value, "time")
        } catch (e: InvocationTargetException) {
            throw e.cause!!
        }
    }

    companion object : ComponentValidator()
}