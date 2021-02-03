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
package solutions.bellatrix.components

import solutions.bellatrix.components.contracts.*
import solutions.bellatrix.plugins.EventListener
import solutions.bellatrix.validations.ValidationComponentExtensions

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
//
//    fun validateHrefIs(value: String) {
//        defaultValidateHrefIs(value!!)
//    }
//
//    fun validateHrefIsSet() {
//        defaultValidateHrefIsSet()
//    }

    companion object {
        val CLICKING = EventListener<ComponentActionEventArgs>()
        val CLICKED = EventListener<ComponentActionEventArgs>()
    }
}