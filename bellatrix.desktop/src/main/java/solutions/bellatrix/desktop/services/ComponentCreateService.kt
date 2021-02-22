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
package solutions.bellatrix.desktop.services

import solutions.bellatrix.desktop.infrastructure.DriverService.getWrappedDriver
import solutions.bellatrix.desktop.components.DesktopComponent
import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.desktop.findstrategies.*

object ComponentCreateService : DesktopService() {
    inline fun <reified TComponent : DesktopComponent> byAccessibilityId(accessibilityId: String): TComponent {
        return create<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : DesktopComponent> byClass(cclass: String): TComponent {
        return create<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : DesktopComponent> byXPath(xpath: String): TComponent {
        return create<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : DesktopComponent> byName(name: String): TComponent {
        return create<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : DesktopComponent> byTag(tag: String): TComponent {
        return create<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : DesktopComponent> byIdContaining(idContaining: String): TComponent {
        return create<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : DesktopComponent> byAutomationId(automationId: String): TComponent {
        return create<TComponent, AutomationIdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : DesktopComponent> allByAccessibilityId(accessibilityId: String): List<TComponent> {
        return createAll<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : DesktopComponent> allByName(name: String): List<TComponent> {
        return createAll<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : DesktopComponent> allByClass(cclass: String): List<TComponent> {
        return createAll<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : DesktopComponent> allByXPath(xpath: String): List<TComponent> {
        return createAll<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : DesktopComponent> allByAutomationId(automationId: String): List<TComponent> {
        return createAll<TComponent, AutomationIdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : DesktopComponent>  allByTag(tag: String): List<TComponent> {
        return createAll<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : DesktopComponent> allByIdContaining(idContaining: String): List<TComponent> {
        return createAll<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : DesktopComponent, reified TFindStrategy : FindStrategy> create(value: String): TComponent {
        val findStrategy = TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        val component: TComponent = InstanceFactory.create()
        component.findStrategy = findStrategy
        component.parentWrappedElement = null
        return component
    }

    inline fun <reified TComponent : DesktopComponent, reified TFindStrategy : FindStrategy> createAll(value: String): List<TComponent> {
        val findStrategy = TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        val nativeElements = findStrategy.findAllElements(getWrappedDriver())
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