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
package solutions.bellatrix.core.plugins.testng

import org.testng.ITestResult
import org.testng.annotations.*
import solutions.bellatrix.core.plugins.Plugin
import solutions.bellatrix.core.plugins.PluginExecutionEngine
import solutions.bellatrix.core.plugins.PluginExecutionEngine.afterClassFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine.afterTestFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine.beforeClassFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine.beforeTestFailed
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postAfterClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postAfterTest
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postBeforeClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.postBeforeTest
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preAfterClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preAfterTest
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preBeforeClass
import solutions.bellatrix.core.plugins.PluginExecutionEngine.preBeforeTest
import solutions.bellatrix.core.plugins.TestResult

@Listeners(TestResultListener::class)
open class BaseTest {
    companion object {
        val CURRENT_TEST_RESULT = ThreadLocal<TestResult>()
        private val CONFIGURATION_EXECUTED = ThreadLocal<Boolean>()
    }

    init {
        CONFIGURATION_EXECUTED.set(false)
    }

    fun addPlugin(plugin: Plugin) {
        PluginExecutionEngine.addPlugin(plugin)
    }

    @BeforeClass
    fun beforeClassCore() {
        try {
            if (!CONFIGURATION_EXECUTED.get()) {
                configure()
                CONFIGURATION_EXECUTED.set(true)
            }

            val testClass: Class<out BaseTest> = this.javaClass
            preBeforeClass(testClass)
            beforeClass()
            postBeforeClass(testClass)
        } catch (e: Exception) {
            beforeClassFailed(e)
        }
    }

    @BeforeMethod
    fun beforeMethodCore(testResult: ITestResult) {
        try {
            val testClass: Class<out BaseTest> = this.javaClass
            val methodInfo = testClass.getMethod(testResult.method.methodName)
            preBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo)
            beforeMethod()
            postBeforeTest(CURRENT_TEST_RESULT.get(), methodInfo)
        } catch (e: Exception) {
            beforeTestFailed(e)
        }
    }

    @AfterMethod
    fun afterMethodCore(testResult: ITestResult) {
        try {
            val testClass: Class<out BaseTest> = this.javaClass
            val methodInfo = testClass.getMethod(testResult.method.methodName)
            preAfterTest(CURRENT_TEST_RESULT.get(), methodInfo)
            afterMethod()
            postAfterTest(CURRENT_TEST_RESULT.get(), methodInfo)
        } catch (e: Exception) {
            afterTestFailed(e)
        }
    }

    @AfterClass
    fun afterClassCore() {
        try {
            val testClass: Class<out BaseTest> = this.javaClass
            preAfterClass(testClass)
            afterClass()
            postAfterClass(testClass)
        } catch (e: Exception) {
            afterClassFailed(e)
        }
    }

    protected open fun configure() {}
    protected open fun beforeClass() {}
    protected open fun afterClass() {}
    protected open fun beforeMethod() {}
    protected open fun afterMethod() {}
}