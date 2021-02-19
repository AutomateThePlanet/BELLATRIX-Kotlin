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
import solutions.bellatrix.plugins.EventListener
import java.util.function.Supplier

class LayoutPreciseValidationBuilder {
    private var actualDistance = 0.0
    private var notificationMessage: String? = null
    private var failedAssertionMessage: String? = null

    constructor(calculateActualDistanceFunction: Supplier<Double>?, notificationMessageFunction: Supplier<String?>, failedAssertionMessageFunction: Supplier<String?>) {
        if (calculateActualDistanceFunction != null) actualDistance = calculateActualDistanceFunction.get()
        notificationMessage = notificationMessageFunction.get()
        failedAssertionMessage = failedAssertionMessageFunction.get()
    }

    constructor(actualDistance: Double) {
        this.actualDistance = actualDistance
    }

    fun equal(expected: Int): FinishValidationBuilder {
        return FinishValidationBuilder({ r: Any? -> actualDistance == expected.toDouble() },
                { buildNotificationValidationMessage(ComparingOperators.EQUAL, expected) }
        ) { buildFailedValidationMessage(ComparingOperators.EQUAL, expected) }
    }

    fun lessThan(expected: Int): FinishValidationBuilder {
        return FinishValidationBuilder({ r: Any? -> actualDistance < expected },
                { buildNotificationValidationMessage(ComparingOperators.LESS_THAN, expected) }
        ) { buildFailedValidationMessage(ComparingOperators.LESS_THAN, expected) }
    }

    fun lessThanOrEqual(expected: Int): FinishValidationBuilder {
        return FinishValidationBuilder({ r: Any? -> actualDistance <= expected },
                { buildNotificationValidationMessage(ComparingOperators.LESS_THAN_EQUAL, expected) }
        ) { buildFailedValidationMessage(ComparingOperators.LESS_THAN_EQUAL, expected) }
    }

    fun greaterThan(expected: Int): FinishValidationBuilder {
        return FinishValidationBuilder({ r: Any? -> actualDistance > expected },
                { buildNotificationValidationMessage(ComparingOperators.GREATER_THAN, expected) }
        ) { buildFailedValidationMessage(ComparingOperators.GREATER_THAN, expected) }
    }

    fun greaterThanOrEqual(expected: Int): FinishValidationBuilder {
        return FinishValidationBuilder({ r: Any? -> actualDistance >= expected },
                { buildNotificationValidationMessage(ComparingOperators.GREATER_THAN_EQUAL, expected) }
        ) { buildFailedValidationMessage(ComparingOperators.GREATER_THAN_EQUAL, expected) }
    }

    private fun buildNotificationValidationMessage(comparingMessage: ComparingOperators, expected: Int): String {
        return String.format("%s%s %d px", notificationMessage, comparingMessage, expected)
    }

    private fun buildFailedValidationMessage(comparingMessage: ComparingOperators, expected: Int): String {
        return String.format("%s%s %d px", failedAssertionMessage, comparingMessage, expected)
    }

    fun validate() {
        Assert.assertTrue(actualDistance > 0, failedAssertionMessage)
        VALIDATED_COMPONENT_LAYOUT.broadcast(LayoutValidationEventArgs(notificationMessage!!))
    }

    companion object {
        val VALIDATED_COMPONENT_LAYOUT = EventListener<LayoutValidationEventArgs>()
    }
}