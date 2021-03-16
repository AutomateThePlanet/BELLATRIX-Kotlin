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
package solutions.bellatrix.ios.services

import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.ios.components.IOSComponent
import solutions.bellatrix.ios.findstrategies.*
import solutions.bellatrix.ios.infrastructure.DriverService

object ComponentCreateService : MobileService() {
    inline fun <reified TComponent : IOSComponent> byId(id: String): TComponent {
        return by<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : IOSComponent> byName(name: String): TComponent {
        return by<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : IOSComponent> byClass(cclass: String): TComponent {
        return by<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : IOSComponent> byXPath(xpath: String): TComponent {
        return by<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : IOSComponent> byIOSNsPredicate(iosNsPredicate: String): TComponent {
        return by<TComponent, IOSNsPredicateFindStrategy>(iosNsPredicate)
    }

    inline fun <reified TComponent : IOSComponent> byValueContaining(value: String): TComponent {
        return by<TComponent, ValueContainingFindStrategy>(value)
    }

    inline fun <reified TComponent : IOSComponent> byIOSClassChain(text: String): TComponent {
        return by<TComponent, IOSClassChainFindStrategy>(text)
    }

    inline fun <reified TComponent : IOSComponent> byAccessibilityIdContaining(accessibilityId: String): TComponent {
        return by<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : IOSComponent> byTag(tag: String): TComponent {
        return by<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : IOSComponent> allById(automationId: String): List<TComponent> {
        return allBy<TComponent, IdFindStrategy>(automationId)
    }

    inline fun <reified TComponent : IOSComponent> allByName(name: String): List<TComponent> {
        return allBy<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : IOSComponent> allByClass(componentClass: Class<TComponent>, cclass: String): List<TComponent> {
        return allBy<TComponent, ClassFindStrategy>(cclass)
    }

    inline fun <reified TComponent : IOSComponent> allByXPath(xpath: String): List<TComponent> {
        return allBy<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : IOSComponent> allByTag(tag: String): List<TComponent> {
        return allBy<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : IOSComponent> allByIOSNsPredicate(iosNsPredicate: String): List<TComponent> {
        return allBy<TComponent, IOSNsPredicateFindStrategy>(iosNsPredicate)
    }

    inline fun <reified TComponent : IOSComponent> allByValueContaining(value: String): List<TComponent> {
        return allBy<TComponent, ValueContainingFindStrategy>(value)
    }

    inline fun <reified TComponent : IOSComponent> allByIOSClassChain(text: String): List<TComponent> {
        return allBy<TComponent, IOSClassChainFindStrategy>(text)
    }

    inline fun <reified TComponent : IOSComponent> allByAccessibilityIdContaining(accessibilityId: String): List<TComponent> {
        return allBy<TComponent, AccessibilityIdFindStrategy>(accessibilityId)
    }

    inline fun <reified TComponent : IOSComponent, reified TFindStrategy : FindStrategy> by(value: String): TComponent {
        val findStrategy = TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        val component: TComponent = InstanceFactory.create()
        component.findStrategy = findStrategy
        component.parentWrappedElement = null
        return component
    }

    inline fun <reified TComponent : IOSComponent, reified TFindStrategy : FindStrategy> allBy(value: String): List<TComponent> {
        val findStrategy = TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        val nativeElements = findStrategy.findAllElements(DriverService.getWrappedIOSDriver())
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