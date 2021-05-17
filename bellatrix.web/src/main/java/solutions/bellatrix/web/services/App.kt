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
import solutions.bellatrix.web.infrastructure.DriverService
import solutions.bellatrix.web.pages.WebPage

object App : AutoCloseable {
    private var disposed = false

    val browser = BrowserService
    val navigate = NavigationService
    val cookies = CookiesService
    val dialogs = DialogService
    val script = JavaScriptService
    val create = ComponentCreateService
    val waitFor = ComponentWaitService
    val requests = ProxyServer

    fun addDriverOptions(key: String, value: String) {
        DriverService.addDriverOptions(key, value)
    }

    inline fun <reified TPage : WebPage<*, *>> goTo(): TPage {
        val page = InstanceFactory.create<TPage>()
        page.open()
        return page
    }

    inline fun <reified TPage : WebPage<*, *>> create(): TPage {
        return InstanceFactory.create<TPage>()
    }

    override fun close() {
        if (disposed) {
            return
        }
        DriverService.close()
        disposed = true
    }
}