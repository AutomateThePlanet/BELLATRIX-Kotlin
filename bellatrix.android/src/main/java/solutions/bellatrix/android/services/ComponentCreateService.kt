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
package solutions.bellatrix.android.services

import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.android.components.AndroidComponent
import solutions.bellatrix.android.findstrategies.*
import solutions.bellatrix.android.findstrategies.FindStrategy
import solutions.bellatrix.android.infrastructure.DriverService

object ComponentCreateService : MobileService() {
    inline fun <reified TComponent : AndroidComponent> byId(id: String): TComponent {
        return by<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : AndroidComponent> byName(name: String): TComponent {
        return by<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : AndroidComponent> byClass(cclass: String): TComponent {
        return by<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : AndroidComponent> byXPath(xpath: String): TComponent {
        return by<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : AndroidComponent> byAndroidUIAutomator(androidUIAutomator: String): TComponent {
        return by<TComponent, AndroidUIAutomatorFindStrategy>(androidUIAutomator)
    }

    inline fun <reified TComponent : AndroidComponent> byText(text: String): TComponent {
        return by<TComponent, TextFindStrategy>(text)
    }

    inline fun <reified TComponent : AndroidComponent> byTextContaining(text: String): TComponent {
        return by<TComponent, TextContainingFindStrategy>(text)
    }

    inline fun <reified TComponent : AndroidComponent> byDescription(description: String): TComponent {
        return by<TComponent, DescriptionFindStrategy>(description)
    }

    inline fun <reified TComponent : AndroidComponent> byDescriptionContaining(description: String): TComponent {
        return by<TComponent, DescriptionContainingFindStrategy>(description)
    }

    inline fun <reified TComponent : AndroidComponent> byTag(tag: String): TComponent {
        return by<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : AndroidComponent> byIdContaining(idContaining: String): TComponent {
        return by<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : AndroidComponent> allById(automationId: String): List<TComponent> {
        return allBy<TComponent, IdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : AndroidComponent> allByName(name: String): List<TComponent> {
        return allBy<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : AndroidComponent> allByClass(componentClass: Class<TComponent>, cclass: String): List<TComponent> {
        return allBy<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : AndroidComponent> allByXPath(xpath: String): List<TComponent> {
        return allBy<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : AndroidComponent> allByAndroidUIAutomator(androidUIAutomator: String): List<TComponent> {
        return allBy<TComponent, AndroidUIAutomatorFindStrategy>(androidUIAutomator)
    }

    inline fun <reified TComponent : AndroidComponent> allByTag(tag: String): List<TComponent> {
        return allBy<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : AndroidComponent> allByText(text: String): List<TComponent> {
        return allBy<TComponent, TextFindStrategy>(text)
    }

    inline fun <reified TComponent : AndroidComponent> allByTextContaining(text: String): List<TComponent> {
        return allBy<TComponent, TextContainingFindStrategy>(text)
    }

    inline fun <reified TComponent : AndroidComponent> allByDescription(description: String): List<TComponent> {
        return allBy<TComponent, DescriptionFindStrategy>(description)
    }

    inline fun <reified TComponent : AndroidComponent> allByDescriptionContaining(description: String): List<TComponent> {
        return allBy<TComponent, DescriptionContainingFindStrategy>(description)
    }

    inline fun <reified TComponent : AndroidComponent> allByIdContaining(idContaining: String): List<TComponent> {
        return allBy<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : AndroidComponent, reified TFindStrategy : FindStrategy> by(value: String): TComponent {
        val findStrategy = TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        val component: TComponent = InstanceFactory.create()
        component.findStrategy = findStrategy
        component.parentWrappedElement = null
        return component
    }

    inline fun <reified TComponent : AndroidComponent, reified TFindStrategy : FindStrategy> allBy(value: String): List<TComponent> {
        val findStrategy = TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        val nativeElements = findStrategy.findAllElements(DriverService.getWrappedAndroidDriver())
        val componentList = mutableListOf<TComponent>()
        for (i in 0 until nativeElements.count()) {
            val component: TComponent = InstanceFactory.create()
            component.findStrategy = findStrategy
            component.elementIndex = i
            componentList.add(component)
        }
        return componentList
    }
}