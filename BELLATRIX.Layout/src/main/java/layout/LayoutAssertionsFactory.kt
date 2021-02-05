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

class LayoutAssertionsFactory(private val component: LayoutComponent) {
    infix fun above(secondComponent: LayoutComponent) : ComparedLayoutElement {
        return ComparedLayoutElement(secondComponent, LayoutAssertion.ABOVE)
    }



    fun assertAboveOf(secondComponent: LayoutComponent, expected: Double) {
        val actualDistance = calculateAboveOfDistance(component, secondComponent)
        Assert.assertEquals(expected, actualDistance, String.format("%s should be %d px above of %s but was %d px.", component.elementName, expected, secondComponent.elementName, actualDistance))
        ASSERTED_ABOVE_OF.broadcast(LayoutTwoComponentsActionEventArgs(component, secondComponent, expected.toString()))
    }

    private fun calculatePercentDifference(num1: Double, num2: Double): Double {
        val percentDifference = (num1 - num2) / ((num1 + num2) / 2) * 100
        return Math.abs(percentDifference)
    }

    private fun calculateRightOfDistance(component: LayoutComponent, secondComponent: LayoutComponent): Double {
        return (secondComponent.location.getX() - (component.location.getX() + component.size.getWidth())).toDouble()
    }

    private fun calculateLeftOfDistance(component: LayoutComponent, secondComponent: LayoutComponent): Double {
        return (component.location.getX() - (secondComponent.location.getX() + secondComponent.size.getWidth())).toDouble()
    }

    private fun calculateAboveOfDistance(component: LayoutComponent, secondComponent: LayoutComponent): Double {
        return (secondComponent.location.getY() - (component.location.getY() + component.size.getHeight())).toDouble()
    }

    private fun calculateBelowOfDistance(component: LayoutComponent, secondComponent: LayoutComponent): Double {
        return (component.location.getY() - (secondComponent.location.getY() + secondComponent.size.getHeight())).toDouble()
    }

    private fun calculateTopInsideOfDistance(innerComponent: LayoutComponent, outerComponent: LayoutComponent): Double {
        return (innerComponent.location.getY() - outerComponent.location.getY()).toDouble()
    }

    private fun calculateBottomInsideOfDistance(innerComponent: LayoutComponent, outerComponent: LayoutComponent): Double {
        return (outerComponent.location.getY() + outerComponent.size.getHeight() - (innerComponent.location.getY() + innerComponent.size.getHeight())).toDouble()
    }

    private fun calculateLeftInsideOfDistance(innerComponent: LayoutComponent, outerComponent: LayoutComponent): Double {
        return (innerComponent.location.getX() - outerComponent.location.getX()).toDouble()
    }

    private fun calculateRightInsideOfDistance(innerComponent: LayoutComponent, outerComponent: LayoutComponent): Double {
        return (outerComponent.location.getX() + outerComponent.size.getWidth() - (innerComponent.location.getX() + innerComponent.size.getWidth())).toDouble()
    }

    companion object {
        // single event?
        val ASSERTED_ABOVE_OF_NO_EXPECTED_VALUE = EventListener<LayoutTwoComponentsNoExpectedActionEventArgs>()
        val ASSERTED_ABOVE_OF = EventListener<LayoutTwoComponentsActionEventArgs>()
        val ASSERTED_ABOVE_OF_BETWEEN = EventListener<LayoutTwoComponentsActionTwoValuesEventArgs>()
        val ASSERTED_ABOVE_OF_GREATER_THAN = EventListener<LayoutTwoComponentsActionEventArgs>()
        val ASSERTED_ABOVE_OF_GREATER_OR_EQUAL = EventListener<LayoutTwoComponentsActionEventArgs>()
        val ASSERTED_ABOVE_OF_LESS_THAN = EventListener<LayoutTwoComponentsActionEventArgs>()
        val ASSERTED_ABOVE_OF_LESS_OR_EQUAL = EventListener<LayoutTwoComponentsActionEventArgs>()
        val ASSERTED_ABOVE_OF_APPROXIMATE = EventListener<LayoutTwoComponentsActionTwoValuesEventArgs>()
    }
}