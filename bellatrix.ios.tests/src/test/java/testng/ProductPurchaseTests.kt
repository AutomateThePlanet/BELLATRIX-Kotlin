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

import org.testng.annotations.Test
import solutions.bellatrix.ios.components.Button
import solutions.bellatrix.ios.components.Label
import solutions.bellatrix.ios.components.TextField
import solutions.bellatrix.ios.infrastructure.ExecutionApp
import solutions.bellatrix.ios.infrastructure.Lifecycle
import solutions.bellatrix.ios.infrastructure.testng.IOSTest

@ExecutionApp(lifecycle = Lifecycle.RESTART_ON_FAIL)
class ProductPurchaseTests : IOSTest() {
    @Test
    fun buttonClicked_when_callClickMethod() {
        val integerA = app.create.byName<TextField>("IntegerA")
        val integerB = app.create.byName<TextField>("IntegerB")
        val button = app.create.byName<Button>("ComputeSumButton")
        val result = app.create.byName<Label>("Answer")
        integerA.setText("100")
        integerB.setText("333")
        button.click()
        result.validateTextIs("433")
    }
}