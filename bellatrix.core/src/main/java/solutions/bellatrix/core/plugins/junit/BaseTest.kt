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
package solutions.bellatrix.core.plugins.junit

import org.junit.Rule
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preBeforeTest
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postBeforeTest
import solutions.bellatrix.core.plugins.PluginExecutionEngine.beforeTestFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preBeforeClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postBeforeClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.beforeClassFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preAfterTest
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postAfterTest
import solutions.bellatrix.core.plugins.PluginExecutionEngine.afterTestFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preAfterClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postAfterClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.afterClassFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.AfterEach
import java.lang.ThreadLocal
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.extension.TestWatcher
import org.junit.runner.Description
import solutions.bellatrix.core.plugins.Plugin
import solutions.bellatrix.core.plugins.TestResult
import java.lang.Exception
import java.util.ArrayList

open class BaseTest {
    companion object {
        private val CURRENT_TEST_RESULT = ThreadLocal<TestResult>()
        private val CONFIGURATION_EXECUTED = ThreadLocal<Boolean>()
        private val ALREADY_EXECUTED_BEFORE_CLASSES = ThreadLocal<MutableList<String>>()

        @AfterAll
        @JvmStatic
        fun afterClassCore(testInfo: TestInfo) {
            try {
                val testClass = testInfo.testClass
                preAfterClass(testClass.get())
                postAfterClass(testClass.get())
            } catch (e: Exception) {
                afterClassFailed(e)
            }
        }
    }

    init {
        CONFIGURATION_EXECUTED.set(false)
        ALREADY_EXECUTED_BEFORE_CLASSES.set(ArrayList())
    }

    fun addPlugin(plugin: Plugin) {
        PluginExecutionEngine.addPlugin(plugin)
    }

    @Rule
    var watchman: TestWatcher = object : TestWatcher {
        protected fun failed(e: Throwable?, description: Description?) {
            CURRENT_TEST_RESULT.set(TestResult.FAILURE)
        }

        protected fun succeeded(description: Description?) {
            CURRENT_TEST_RESULT.set(TestResult.SUCCESS)
        }
    }

    @BeforeEach
    fun beforeMethodCore(testInfo: TestInfo) {
        try {
            if (!ALREADY_EXECUTED_BEFORE_CLASSES.get().contains(testInfo.testClass.get().name)) {
                beforeClassCore()
                ALREADY_EXECUTED_BEFORE_CLASSES.get().add(testInfo.testClass.get().name)
            }
            val testClass: Class<out BaseTest?> = this.javaClass
            val methodInfo = testClass.getMethod(testInfo.testMethod.get().name)
            preBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo)
            beforeMethod()
            postBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo)
        } catch (e: Exception) {
            beforeTestFailed(e)
        }
    }

    fun beforeClassCore() {
        try {
            if (!CONFIGURATION_EXECUTED.get()) {
                configure()
                CONFIGURATION_EXECUTED.set(true)
            }
            val testClass: Class<out BaseTest?> = this.javaClass
            preBeforeClass(testClass)
            beforeAll()
            postBeforeClass(testClass)
        } catch (e: Exception) {
            beforeClassFailed(e)
        }
    }

    @AfterEach
    fun afterMethodCore(testInfo: TestInfo) {
        try {
            val testClass: Class<out BaseTest?> = this.javaClass
            val methodInfo = testClass.getMethod(testInfo.testMethod.get().name)
            preAfterTest(CURRENT_TEST_RESULT.get(), methodInfo)
            afterMethod()
            postAfterTest(CURRENT_TEST_RESULT.get(), methodInfo)
        } catch (e: Exception) {
            afterTestFailed(e)
        }
    }

    protected open fun configure() {}
    protected fun beforeAll() {}
    protected fun beforeMethod() {}
    protected fun afterMethod() {}
}