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

object PluginExecutionEngine {
    private var plugins = mutableListOf<Plugin>()

    @JvmStatic
    fun addPlugin(plugin: Plugin) {
        if (!plugins.any { it.javaClass.name === plugin.javaClass.name }) {
            plugins.add(plugin)
        }
    }

    fun removePlugin(plugin: Plugin) {
        plugins.remove(plugin)
    }

    @JvmStatic
    fun preBeforeClass(type: Class<*>) {
        for (currentObserver in plugins) {
            currentObserver.preBeforeClass(type)
        }
    }

    @JvmStatic
    fun postBeforeClass(type: Class<*>) {
        for (currentObserver in plugins) {
            currentObserver.postBeforeClass(type)
        }
    }

    @JvmStatic
    fun beforeClassFailed(e: Exception) {
        for (currentObserver in plugins) {
            currentObserver.beforeClassFailed(e)
        }
    }

    fun preBeforeTest(result: TestResult, memberInfo: Method) {
        for (currentObserver in plugins) {
            currentObserver.preBeforeTest(result, memberInfo)
        }
    }

    fun postBeforeTest(result: TestResult, memberInfo: Method) {
        for (currentObserver in plugins) {
            currentObserver.postBeforeTest(result, memberInfo)
        }
    }

    fun beforeTestFailed(e: Exception) {
        for (currentObserver in plugins) {
            currentObserver.beforeTestFailed(e)
        }
    }

    fun preAfterTest(result: TestResult, memberInfo: Method) {
        for (currentObserver in plugins) {
            currentObserver.preAfterTest(result, memberInfo)
        }
    }

    fun postAfterTest(result: TestResult, memberInfo: Method) {
        for (currentObserver in plugins) {
            currentObserver.postAfterTest(result, memberInfo)
        }
    }

    fun afterTestFailed(e: Exception) {
        for (currentObserver in plugins) {
            currentObserver.afterTestFailed(e)
        }
    }

    fun preAfterClass(type: Class<*>) {
        for (currentObserver in plugins) {
            currentObserver.preAfterClass(type)
        }
    }

    fun postAfterClass(type: Class<*>) {
        for (currentObserver in plugins) {
            currentObserver.postAfterClass(type)
        }
    }

    fun afterClassFailed(e: Exception) {
        for (currentObserver in plugins) {
            currentObserver.afterClassFailed(e)
        }
    }
}