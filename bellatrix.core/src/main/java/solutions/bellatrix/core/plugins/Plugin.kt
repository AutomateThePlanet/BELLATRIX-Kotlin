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
package solutions.bellatrix.core.plugins

import java.lang.reflect.Method

abstract class Plugin {
    init {
        PluginExecutionEngine.addPlugin(this)
    }

    open fun preBeforeClass(type: Class<*>) {}
    open fun postBeforeClass(type: Class<*>) {}
    open fun beforeClassFailed(e: Exception) {}
    open fun preBeforeTest(testResult: TestResult, memberInfo: Method) {}
    open fun postBeforeTest(testResult: TestResult, memberInfo: Method) {}
    open fun beforeTestFailed(e: Exception?) {}
    open fun preAfterTest(testResult: TestResult, memberInfo: Method) {}
    open fun postAfterTest(testResult: TestResult, memberInfo: Method) {}
    open fun afterTestFailed(e: Exception) {}
    open fun preAfterClass(type: Class<*>) {}
    open fun postAfterClass(type: Class<*>) {}
    open fun afterClassFailed(e: Exception) {}
}