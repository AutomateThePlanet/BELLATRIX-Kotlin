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
import solutions.bellatrix.web.validations.WebValidator

interface ComponentHref : Component {
    val href: String

    fun validateHrefIs(value: String) {
        defaultValidateAttributeIs(this as WebComponent, href, value, "href")
    }

    fun validateHrefIsSet() {
        defaultValidateAttributeIsSet(this as WebComponent, href, "href")
    }

    fun validateHrefNotSet() {
        defaultValidateAttributeNotSet(this as WebComponent, href, "href")
    }

    fun validateHrefContains(value: String) {
        defaultValidateAttributeContains(this as WebComponent, href, value, "href")
    }

    fun validateHrefNotContains(value: String) {
        defaultValidateAttributeNotContains(this as WebComponent, href, value, "href")
    }

    companion object : WebValidator()
}