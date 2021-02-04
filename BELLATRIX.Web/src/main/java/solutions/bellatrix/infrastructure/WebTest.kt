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
package solutions.bellatrix.infrastructure

import solutions.bellatrix.plugins.BaseTest
import solutions.bellatrix.services.App
import org.testng.annotations.BeforeSuite
import plugins.video.VideoPlugin
import solutions.bellatrix.components.listeners.BddLogging
import solutions.bellatrix.components.listeners.HighlightElements
import solutions.bellatrix.validations.ValidationsBddLogging

open class WebTest : BaseTest() {
    val app
    get() = App

    @BeforeSuite
    fun beforeSuiteCore() {
        addPlugin(BrowserLifecyclePlugin.of())
        addPlugin(WebScreenshotPlugin.of())
        addPlugin(WebVideoPlugin.of())
        BddLogging.addPlugin()
        ValidationsBddLogging.addPlugin()
        HighlightElements.addPlugin()
        beforeSuite();
    }

    fun beforeSuite() {
    }
}