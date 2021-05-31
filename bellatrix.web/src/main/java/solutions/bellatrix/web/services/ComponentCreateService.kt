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
package solutions.bellatrix.web.services

import solutions.bellatrix.core.utilities.InstanceFactory
import solutions.bellatrix.web.components.WebComponent
import solutions.bellatrix.web.findstrategies.*
import solutions.bellatrix.web.infrastructure.DriverService.wrappedDriver

object ComponentCreateService : WebService() {
    inline fun <reified TComponent : WebComponent> byId(id: String): TComponent {
        return create<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : WebComponent> byAttributeContaining(attributeName: String, value: String): TComponent {
        return create<TComponent, AttributeContainingFindStrategy>(attributeName, value)
    }

    inline fun <reified TComponent : WebComponent> byIdEndingWith(idEnding: String): TComponent {
        return create<TComponent, IdEndingWithFindStrategy>(idEnding)
    }

    inline fun <reified TComponent : WebComponent> byCss(css: String): TComponent {
        return create<TComponent, CssFindStrategy>(css)
    }

    inline fun <reified TComponent : WebComponent> byClass(className: String): TComponent {
        return create<TComponent, ClassFindStrategy>(className)
    }

    inline fun <reified TComponent : WebComponent> byName(name: String): TComponent {
        return create<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : WebComponent> byValueContaining(valueContaining: String): TComponent {
        return create<TComponent, ValueContainingFindStrategy>(valueContaining)
    }

    inline fun <reified TComponent : WebComponent> byClassContaining(classNameContaining: String): TComponent {
        return create<TComponent, ClassContainingFindStrategy>(classNameContaining)
    }

    inline fun <reified TComponent : WebComponent> byXPath(xpath: String): TComponent {
        return create<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : WebComponent> byLinkText(linkText: String): TComponent {
        return create<TComponent, LinkTextFindStrategy>(linkText)
    }

    inline fun <reified TComponent : WebComponent> byLinkTextContaining(linkTextContaining: String): TComponent {
        return create<TComponent, LinkTextContainingFindStrategy>(linkTextContaining)
    }

    inline fun <reified TComponent : WebComponent> byTag(tag: String): TComponent {
        return create<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : WebComponent> byIdContaining(idContaining: String): TComponent {
        return create<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : WebComponent> byInnerTextContaining(innerText: String): TComponent {
        return create<TComponent, InnerTextContainingFindStrategy>(innerText)
    }

    inline fun <reified TComponent : WebComponent> allById(id: String): List<TComponent> {
        return createAll<TComponent, IdFindStrategy>(id)
    }

    inline fun <reified TComponent : WebComponent> allByAttributeContaining(attributeName: String, value: String): List<TComponent> {
        return createAll<TComponent, AttributeContainingFindStrategy>(attributeName, value)
    }

    inline fun <reified TComponent : WebComponent> allByIdEndingWith(idEnding: String): List<TComponent> {
        return createAll<TComponent, IdEndingWithFindStrategy>(idEnding)
    }

    inline fun <reified TComponent : WebComponent> allBbyCss(css: String): List<TComponent> {
        return createAll<TComponent, CssFindStrategy>(css)
    }

    inline fun <reified TComponent : WebComponent> allByClass(className: String): List<TComponent> {
        return createAll<TComponent, ClassFindStrategy>(className)
    }

    inline fun <reified TComponent : WebComponent> allByName(name: String): List<TComponent> {
        return createAll<TComponent, NameFindStrategy>(name)
    }

    inline fun <reified TComponent : WebComponent> allByValueContaining(valueContaining: String): List<TComponent> {
        return createAll<TComponent, ValueContainingFindStrategy>(valueContaining)
    }

    inline fun <reified TComponent : WebComponent> allByClassContaining(classNameContaining: String): List<TComponent> {
        return createAll<TComponent, ClassContainingFindStrategy>(classNameContaining)
    }

    inline fun <reified TComponent : WebComponent> allByXPath(xpath: String): List<TComponent> {
        return createAll<TComponent, XPathFindStrategy>(xpath)
    }

    inline fun <reified TComponent : WebComponent> allByLinkText(linkText: String): List<TComponent> {
        return createAll<TComponent, LinkTextFindStrategy>(linkText)
    }

    inline fun <reified TComponent : WebComponent> allByLinkTextContaining(linkTextContaining: String): List<TComponent> {
        return createAll<TComponent, LinkTextContainingFindStrategy>(linkTextContaining)
    }

    inline fun <reified TComponent : WebComponent> allByTag(tag: String): List<TComponent> {
        return createAll<TComponent, TagFindStrategy>(tag)
    }

    inline fun <reified TComponent : WebComponent> allByIdContaining(idContaining: String): List<TComponent> {
        return createAll<TComponent, IdContainingFindStrategy>(idContaining)
    }

    inline fun <reified TComponent : WebComponent> allByInnerTextContaining(innerText: String): List<TComponent> {
        return createAll<TComponent, InnerTextContainingFindStrategy>(innerText)
    }

    inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> create(value: String, arg: String? = null): TComponent {
        val findStrategy: TFindStrategy = if (arg != null) {
            TFindStrategy::class.java.constructors[0].newInstance(value, arg) as TFindStrategy
        } else {
            TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        }
        val component: TComponent = InstanceFactory.create()
        component.findStrategy = findStrategy
        component.parentWrappedElement = null
        return component
    }

    inline fun <reified TComponent : WebComponent, reified TFindStrategy : FindStrategy> createAll(value: String, arg: String? = null): List<TComponent> {
        val findStrategy: TFindStrategy = if (arg != null) {
            TFindStrategy::class.java.constructors[0].newInstance(value, arg) as TFindStrategy
        } else {
            TFindStrategy::class.java.constructors[0].newInstance(value) as TFindStrategy
        }
        val nativeElements = wrappedDriver().findElements(findStrategy.convert())
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