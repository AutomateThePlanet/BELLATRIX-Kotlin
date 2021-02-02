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
import solutions.bellatrix.services.NavigationService
import solutions.bellatrix.utilities.InstanceFactory

abstract class WebPage<ComponentsT : PageComponents, AssertsT : PageAsserts<ComponentsT>> {
    fun browser(): BrowserService {
        return BrowserService
    }

    fun create(): ComponentCreateService {
        return ComponentCreateService
    }

    inline fun <reified ComponentsT> elements(): ComponentsT {
        return InstanceFactory.create()
    }

    inline fun <reified AssertsT> asserts(): AssertsT {
        return InstanceFactory.create()
    }

    fun navigate(): NavigationService {
        return NavigationService
    }

    protected abstract val url: String

    fun open() {
        navigate().to(url)
        waitForPageLoad()
    }

    protected open fun waitForPageLoad() {}
}