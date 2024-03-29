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
package layout

import org.testng.Assert
import solutions.bellatrix.core.plugins.EventListener
import java.util.function.Predicate
import java.util.function.Supplier

class FinishValidationBuilder(private var comparingFunction: Predicate<Boolean>, notificationMessageFunction: Supplier<String>, failedAssertionMessageFunction: Supplier<String>) {
    private var notificationMessage: String = ""
    private var failedAssertionMessage: String = ""

    init {
        notificationMessage = notificationMessageFunction.get()
        failedAssertionMessage = failedAssertionMessageFunction.get()
    }

    fun validate() {
        Assert.assertTrue(comparingFunction.test(true), failedAssertionMessage)
        VALIDATED_COMPONENT_LAYOUT.broadcast(LayoutValidationEventArgs(notificationMessage))
    }

    companion object {
        val VALIDATED_COMPONENT_LAYOUT = EventListener<LayoutValidationEventArgs>()
    }
}