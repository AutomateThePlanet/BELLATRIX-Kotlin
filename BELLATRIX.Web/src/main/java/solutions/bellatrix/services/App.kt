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

import solutions.bellatrix.infrastructure.DriverService
import java.lang.AutoCloseable
import solutions.bellatrix.pages.WebPage
import solutions.bellatrix.utilities.InstanceFactory

class App : AutoCloseable {
    private var disposed = false
    fun navigate(): NavigationService {
        return NavigationService
    }

    fun browser(): BrowserService {
        return BrowserService
    }

    fun cookies(): CookiesService {
        return CookiesService
    }

    fun dialogs(): DialogService {
        return DialogService
    }

    fun script(): JavaScriptService {
        return JavaScriptService
    }

    fun create(): ComponentCreateService {
        return ComponentCreateService
    }

    fun waitFor(): ComponentWaitService {
        return ComponentWaitService
    }

    fun addDriverOptions(key: String, value: String) {
        DriverService.addDriverOptions(key, value)
    }

    inline fun <reified TPage : WebPage<*, *>> goTo(vararg args: Any): TPage {
        val page = InstanceFactory.create<TPage>(args)
        page.open()
        return page
    }

    inline fun <reified TPage : WebPage<*, *>> create(vararg args: Any): TPage {
        return InstanceFactory.create<TPage>(args)
    }

    override fun close() {
        if (disposed) {
            return
        }
        DriverService.close()
        disposed = true
    }
}