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
package solutions.bellatrix.plugins

import solutions.bellatrix.plugins.PluginExecutionEngine.addPlugin
import solutions.bellatrix.plugins.PluginExecutionEngine.preBeforeClass
import solutions.bellatrix.plugins.PluginExecutionEngine.postBeforeClass
import solutions.bellatrix.plugins.PluginExecutionEngine.beforeClassFailed
import solutions.bellatrix.plugins.PluginExecutionEngine.preBeforeTest
import solutions.bellatrix.plugins.PluginExecutionEngine.postBeforeTest
import solutions.bellatrix.plugins.PluginExecutionEngine.beforeTestFailed
import solutions.bellatrix.plugins.PluginExecutionEngine.preAfterTest
import solutions.bellatrix.plugins.PluginExecutionEngine.postAfterTest
import solutions.bellatrix.plugins.PluginExecutionEngine.afterTestFailed
import solutions.bellatrix.plugins.PluginExecutionEngine.preAfterClass
import solutions.bellatrix.plugins.PluginExecutionEngine.postAfterClass
import solutions.bellatrix.plugins.PluginExecutionEngine.afterClassFailed
import org.testng.ITestResult
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeClass
import java.lang.Exception

open class BaseTest {
//    private val plugins = mutableListOf<Plugin>()
//    private var testResult: ITestResult? = null

    fun addPlugin(plugin: Plugin?) {
        PluginExecutionEngine.addPlugin(plugin!!)
    }

    @BeforeClass
    fun beforeClassCore() {
        try {
            configure()
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
            preBeforeTest(testResult, methodInfo)
            beforeMethod()
            postBeforeTest(testResult, methodInfo)
        } catch (e: Exception) {
            beforeTestFailed(e)
        }
    }

    @AfterMethod
    fun afterMethodCore(testResult: ITestResult) {
        try {
            val testClass: Class<out BaseTest> = this.javaClass
            val methodInfo = testClass.getMethod(testResult.method.methodName)
            preAfterTest(testResult, methodInfo)
            afterMethod()
            postAfterTest(testResult, methodInfo)
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