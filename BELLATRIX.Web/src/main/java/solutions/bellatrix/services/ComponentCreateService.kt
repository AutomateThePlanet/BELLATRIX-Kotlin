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
package solutions.bellatrix.services

import solutions.bellatrix.infrastructure.DriverService.wrappedDriver
import solutions.bellatrix.components.WebComponent
import solutions.bellatrix.findstrategies.FindStrategy
import solutions.bellatrix.utilities.InstanceFactory
import solutions.bellatrix.findstrategies.IdFindStrategy
import solutions.bellatrix.findstrategies.CssFindStrategy
import solutions.bellatrix.findstrategies.ClassFindStrategy
import solutions.bellatrix.findstrategies.XPathFindStrategy
import solutions.bellatrix.findstrategies.LinkTextFindStrategy
import solutions.bellatrix.findstrategies.TagFindStrategy
import solutions.bellatrix.findstrategies.IdContainingFindStrategy
import solutions.bellatrix.findstrategies.InnerTextContainsFindStrategy
import org.openqa.selenium.WebElement
import java.util.ArrayList

object ComponentCreateService : WebService {
    fun <TComponent : WebComponent?, TFindStrategy : FindStrategy?> create(findStrategyClass: Class<TFindStrategy>?, componentClass: Class<TComponent>?, vararg args: Any?): TComponent {
        val findStrategy: Unit = InstanceFactory.create(findStrategyClass, args)
        return by(componentClass, findStrategy)
    }

    fun <TComponent : WebComponent?, TFindStrategy : FindStrategy?> allBy(findStrategyClass: Class<TFindStrategy>?, componentClass: Class<TComponent>?, vararg args: Any?): List<TComponent> {
        val findStrategy: Unit = InstanceFactory.create(findStrategyClass, args)
        return allBy(componentClass, findStrategy)
    }

    fun <TComponent : WebComponent?> byId(componentClass: Class<TComponent>?, id: String?): TComponent {
        return by(componentClass, IdFindStrategy(id!!))
    }

    fun <TComponent : WebComponent?> byCss(componentClass: Class<TComponent>?, css: String?): TComponent {
        return by(componentClass, CssFindStrategy(css!!))
    }

    fun <TComponent : WebComponent?> byClass(componentClass: Class<TComponent>?, cclass: String?): TComponent {
        return by(componentClass, ClassFindStrategy(cclass))
    }

    fun <TComponent : WebComponent?> byXPath(componentClass: Class<TComponent>?, xpath: String?): TComponent {
        return by(componentClass, XPathFindStrategy(xpath!!))
    }

    fun <TComponent : WebComponent?> byLinkText(componentClass: Class<TComponent>?, linkText: String?): TComponent {
        return by(componentClass, LinkTextFindStrategy(linkText!!))
    }

    fun <TComponent : WebComponent?> byTag(componentClass: Class<TComponent>?, tag: String?): TComponent {
        return by(componentClass, TagFindStrategy(tag!!))
    }

    fun <TComponent : WebComponent?> byIdContaining(componentClass: Class<TComponent>?, idContaining: String?): TComponent {
        return by(componentClass, IdContainingFindStrategy(idContaining!!))
    }

    fun <TComponent : WebComponent?> byInnerTextContaining(componentClass: Class<TComponent>?, innerText: String?): TComponent {
        return by(componentClass, InnerTextContainsFindStrategy(innerText))
    }

    fun <TComponent : WebComponent?> allById(componentClass: Class<TComponent>?, id: String?): List<TComponent> {
        return allBy(componentClass, IdFindStrategy(id!!))
    }

    fun <TComponent : WebComponent?> allByCss(componentClass: Class<TComponent>?, css: String?): List<TComponent> {
        return allBy(componentClass, CssFindStrategy(css!!))
    }

    fun <TComponent : WebComponent?> allByClass(componentClass: Class<TComponent>?, cclass: String?): List<TComponent> {
        return allBy(componentClass, ClassFindStrategy(cclass))
    }

    fun <TComponent : WebComponent?> allByXPath(componentClass: Class<TComponent>?, xpath: String?): List<TComponent> {
        return allBy(componentClass, XPathFindStrategy(xpath!!))
    }

    fun <TComponent : WebComponent?> allByLinkText(componentClass: Class<TComponent>?, linkText: String?): List<TComponent> {
        return allBy(componentClass, LinkTextFindStrategy(linkText!!))
    }

    fun <TComponent : WebComponent?> allByTag(componentClass: Class<TComponent>?, tag: String?): List<TComponent> {
        return allBy(componentClass, TagFindStrategy(tag!!))
    }

    fun <TComponent : WebComponent?> allByIdContaining(componentClass: Class<TComponent>?, idContaining: String?): List<TComponent> {
        return allBy(componentClass, IdContainingFindStrategy(idContaining!!))
    }

    fun <TComponent : WebComponent?> allByInnerTextContaining(componentClass: Class<TComponent>?, innerText: String?): List<TComponent> {
        return allBy(componentClass, InnerTextContainsFindStrategy(innerText))
    }

    fun <TComponent : WebComponent?, TFindStrategy : FindStrategy?> by(componentClass: Class<TComponent>?, findStrategy: TFindStrategy): TComponent {
        val component: Unit = InstanceFactory.create(componentClass)
        component.setFindStrategy(findStrategy)
        return component
    }

    fun <TComponent : WebComponent?, TFindStrategy : FindStrategy?> allBy(componentClass: Class<TComponent>?, findStrategy: TFindStrategy): List<TComponent> {
        val nativeElements = wrappedDriver().findElements(findStrategy!!.convert())
        val componentList: MutableList<TComponent> = ArrayList()
        for (i in 0 until nativeElements.stream().count()) {
            val component: Unit = InstanceFactory.create(componentClass)
            component.setFindStrategy(findStrategy)
            component.setElementIndex(i)
            componentList.add(component)
        }
        return componentList
    }
}