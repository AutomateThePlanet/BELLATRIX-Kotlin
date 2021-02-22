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

import solutions.bellatrix.web.components.contracts.*
import solutions.bellatrix.core.plugins.EventListener

class Anchor : WebComponent(), ComponentHref, ComponentText, ComponentHtml, ComponentTarget, ComponentRel {
    override val componentClass: Class<*>
        get() = javaClass

    override val href: String
        get() = defaultGetHref()

    override val text: String
        get() = defaultGetText()

    override val html: String
        get() = defaultGetInnerHtmlAttribute()

    override val target: String
        get() = defaultGetTargetAttribute()

    override val rel: String
        get() = defaultGetRelAttribute()

    fun click() {
        defaultClick(CLICKING, CLICKED)
    }

//    // validate inner HTML
//    fun validateHtmlIs(value: String) {
//        defaultValidateAttributeIs(this::html, value, "inner HTML")
//    }
//
//    fun validateHtmlContains(value: String) {
//        defaultValidateAttributeContains(this::html, value, "inner HTML")
//    }
//
//    fun validateHtmlNotContains(value: String) {
//        defaultValidateAttributeNotContains(this::html, value, "inner HTML")
//    }
//
//    // validate inner text
//    fun validateTextIs(value: String) {
//        defaultValidateAttributeIs(this::text, value, "inner text")
//    }
//
//    fun validateTextIsSet() {
//        defaultValidateAttributeSet(this::text, "inner text")
//    }
//
//    fun validateTextContains(value: String) {
//        defaultValidateAttributeContains(this::text, value, "inner text")
//    }
//
//    fun validateTextNotContains(value: String) {
//        defaultValidateAttributeNotContains(this::text, value, "inner text")
//    }
//
//    // validate HREF
//    fun validateHrefIs(value: String) {
//        defaultValidateAttributeIs(this::href, value, "href")
//    }
//
//    fun validateHrefIsSet() {
//        defaultValidateAttributeSet(this::href, "href")
//    }
//
//    fun validateHrefNotSet() {
//        defaultValidateAttributeNotSet(this::href, "href")
//    }
//
//    // validate Target
//    fun validateTargetIs(value: String) {
//        defaultValidateAttributeIs(this::target, value, "target")
//    }
//
//    fun validateTargetSet() {
//        defaultValidateAttributeSet(this::target, "target")
//    }
//
//    fun validateTargetNotSet() {
//        defaultValidateAttributeNotSet(this::target, "target")
//    }
//
//    // validate Rel
//    fun validateRelIs(value: String) {
//        defaultValidateAttributeIs(this::rel, value, "rel")
//    }
//
//    fun validateRelSet() {
//        defaultValidateAttributeSet(this::rel, "rel")
//    }
//
//    fun validateRelNotSet() {
//        defaultValidateAttributeNotSet(this::rel, "rel")
//    }

    companion object {
        val CLICKING = EventListener<ComponentActionEventArgs>()
        val CLICKED = EventListener<ComponentActionEventArgs>()
    }
}