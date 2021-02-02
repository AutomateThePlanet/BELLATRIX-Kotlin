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
package solutions.bellatrix.pages


import solutions.bellatrix.services.BrowserService
import solutions.bellatrix.services.ComponentCreateService
import java.lang.Exception
import java.lang.reflect.ParameterizedType

abstract class WebSection<ComponentsT : PageComponents, AssertionsT : PageAsserts<ComponentsT>> {
    fun browser(): BrowserService {
        return BrowserService
    }

    fun create(): ComponentCreateService {
        return ComponentCreateService
    }

    fun elements(): ComponentsT? {
        return try {
            val elementsClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<ComponentsT>
            elementsClass.getDeclaredConstructor().newInstance()
        } catch (e: Exception) {
            null
        }
    }

    fun asserts(): AssertionsT? {
        return try {
            val assertionsClass = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<AssertionsT>
            assertionsClass.getDeclaredConstructor().newInstance()
        } catch (e: Exception) {
            null
        }
    }
}