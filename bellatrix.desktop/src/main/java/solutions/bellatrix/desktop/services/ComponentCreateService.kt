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
    inline fun <reified TComponent : DesktopComponent> createByAccessibilityId(accessibilityId: String): TComponent {
        return create<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : DesktopComponent> createByClass(cclass: String): TComponent {
        return create<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : DesktopComponent> createByXPath(componentClass: Class<TComponent>, xpath: String): TComponent {
        return create<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : DesktopComponent> createByName(name: String): TComponent {
        return create<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : DesktopComponent> createByTag(tag: String): TComponent {
        return create<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : DesktopComponent> createByIdContaining(idContaining: String): TComponent {
        return create<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : DesktopComponent> createByAutomationId(automationId: String): TComponent {
        return create<TComponent, AutomationIdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByAccessibilityId(accessibilityId: String): List<TComponent> {
        return createAll<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByName(name: String): List<TComponent> {
        return createAll<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByClass(cclass: String): List<TComponent> {
        return createAll<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByXPath(xpath: String): List<TComponent> {
        return createAll<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByAutomationId(automationId: String): List<TComponent> {
        return createAll<TComponent, AutomationIdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : DesktopComponent>  createAllByTag(tag: String): List<TComponent> {
        return createAll<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : DesktopComponent> createAllByIdContaining(idContaining: String): List<TComponent> {
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