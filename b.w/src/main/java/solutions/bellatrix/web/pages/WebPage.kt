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
package solutions.bellatrix.web.pages

import solutions.bellatrix.web.services.BrowserService
import solutions.bellatrix.web.services.ComponentCreateService
import solutions.bellatrix.web.services.NavigationService

abstract class WebPage<MapT : PageMap, AssertsT : PageAsserts<MapT>> {
    val browser = BrowserService
    var create = ComponentCreateService
    var navigate = NavigationService
    abstract val map: MapT
    abstract val asserts: AssertsT

    protected abstract val url: String

    fun open() {
        navigate.to(url)
        waitForPageLoad()
    }

    protected open fun waitForPageLoad() {}
}