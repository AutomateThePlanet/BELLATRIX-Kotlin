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
package testng

import org.testng.Assert
import org.testng.annotations.Test
import solutions.bellatrix.desktop.components.Button
import solutions.bellatrix.desktop.components.TextField
import solutions.bellatrix.desktop.infrastructure.ExecutionApp
import solutions.bellatrix.desktop.infrastructure.Lifecycle
import solutions.bellatrix.desktop.infrastructure.testng.DesktopTest

@ExecutionApp(appPath = "Microsoft.WindowsCalculator_8wekyb3d8bbwe!App", lifecycle = Lifecycle.RESTART_ON_FAIL)
class CalculatorTests : DesktopTest() {
    @Test
    fun addition() {
        app.create.byName<Button>("Five").click()
        app.create.byName<Button>("Plus").click()
        app.create.byName<Button>("Seven").click()
        app.create.byName<Button>("Equals").click()
        val calculatorResult = calculatorResultText
        Assert.assertEquals(calculatorResult, "12")
    }

    @Test
    fun division() {
        app.create.byAccessibilityId<Button>("num8Button").click()
        app.create.byAccessibilityId<Button>("num8Button").click()
        app.create.byAccessibilityId<Button>("divideButton").click()
        app.create.byAccessibilityId<Button>("num1Button").click()
        app.create.byAccessibilityId<Button>("num1Button").click()
        app.create.byAccessibilityId<Button>("equalButton").click()
        val calculatorResult = calculatorResultText
        Assert.assertEquals(calculatorResult, "8")
    }

    @Test
    fun multiplication() {
        app.create.byXPath<Button>( "//Button[@Name='Nine']").click()
        app.create.byXPath<Button>( "//Button[@Name='Multiply by']").click()
        app.create.byXPath<Button>( "//Button[@Name='Nine']").click()
        app.create.byXPath<Button>("//Button[@Name='Equals']").click()
        val calculatorResult = calculatorResultText
        Assert.assertEquals(calculatorResult, "81")
    }

    @Test
    fun subtraction() {
        app.create.byXPath<Button>("//Button[@AutomationId='num9Button']").click()
        app.create.byXPath<Button>("//Button[@AutomationId='minusButton']").click()
        app.create.byXPath<Button>("//Button[@AutomationId='num1Button']").click()
        app.create.byXPath<Button>("//Button[@AutomationId='equalButton']").click()
        val calculatorResult = calculatorResultText
        Assert.assertEquals(calculatorResult, "8")
    }

    private val calculatorResultText: String
        get() = app.create.byAccessibilityId<TextField>("CalculatorResults").text.replace("Display is", "").trim()
}