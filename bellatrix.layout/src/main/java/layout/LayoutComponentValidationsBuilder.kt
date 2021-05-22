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

import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors

abstract class LayoutComponentValidationsBuilder() : LayoutComponent {
    fun above(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateAboveOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.ABOVE) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.ABOVE) })
    }

    fun right(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateRightOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.RIGHT) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.RIGHT) })
    }

    fun left(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateLeftOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.LEFT) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.LEFT) })
    }

    fun below(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateBelowOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.BELOW) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.BELOW) })
    }

    fun topInside(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateTopInsideOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.TOP_INSIDE) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.TOP_INSIDE) })
    }

    fun inside(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateTopInsideOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.INSIDE) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.INSIDE) })
    }

    fun bottomInside(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateBottomInsideOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.BOTTOM_INSIDE) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.BOTTOM_INSIDE) })
    }

    fun leftInside(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateLeftInsideOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.LEFT_INSIDE) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.LEFT_INSIDE) })
    }

    fun rightInside(secondLayoutComponent: LayoutComponent): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(
                { calculateRightInsideOfDistance(this, secondLayoutComponent) },
                { buildNotificationValidationMessage(secondLayoutComponent, LayoutOptions.RIGHT_INSIDE) },
                { buildFailedValidationMessage(secondLayoutComponent, LayoutOptions.RIGHT_INSIDE) })
    }

    fun alignedVerticallyAll(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineRightY: Int = location.getX() + size.getWidth() / 2
        val baseLineLeftY: Int = location.getX()
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { s: Any? -> true }
        Arrays.stream(layoutComponents).forEach { c: LayoutComponent ->
            val rightX: Int = c.location.getX() + c.size.getWidth() / 2
            val leftX: Int = c.location.getX()
            combinedPredicate.and({ (baseLineRightY == rightX) }).and({ r: Any? -> (baseLineLeftY == leftX) })
        }
        return FinishValidationBuilder(combinedPredicate,
                {
                    buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineRightY, LayoutOptions.ALIGNED_VERTICALLY_RIGHT) +
                            buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineLeftY, LayoutOptions.ALIGNED_VERTICALLY_LEFT)
                },
                {
                    buildFailedAlignValidationMessage(comparingComponentsNames, baseLineRightY, LayoutOptions.ALIGNED_VERTICALLY_RIGHT) +
                            buildFailedAlignValidationMessage(comparingComponentsNames, baseLineLeftY, LayoutOptions.ALIGNED_VERTICALLY_LEFT)
                })
    }

    fun alignedVerticallyCentered(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineRightY: Int = location.getX() + size.getWidth() / 2
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { s: Any? -> true }
        Arrays.stream(layoutComponents).forEach({ c: LayoutComponent ->
            val rightX: Int = c.location.getX() + c.size.getWidth() / 2
            combinedPredicate.and({ r: Any? -> (baseLineRightY == rightX) })
        })
        return FinishValidationBuilder(combinedPredicate,
                { buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineRightY, LayoutOptions.ALIGNED_VERTICALLY_CENTERED) },
                { buildFailedAlignValidationMessage(comparingComponentsNames, baseLineRightY, LayoutOptions.ALIGNED_VERTICALLY_CENTERED) })
    }

    fun alignedVerticallyRight(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineRightY: Int = location.getX() + size.getWidth()
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { s: Any? -> true }
        Arrays.stream(layoutComponents).forEach({ c: LayoutComponent ->
            val rightX: Int = c.location.getX() + c.size.getWidth()
            combinedPredicate.and({ r: Any? -> (baseLineRightY == rightX) })
        })
        return FinishValidationBuilder(combinedPredicate,
                { buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineRightY, LayoutOptions.ALIGNED_VERTICALLY_RIGHT) },
                { buildFailedAlignValidationMessage(comparingComponentsNames, baseLineRightY, LayoutOptions.ALIGNED_VERTICALLY_RIGHT) })
    }

    fun alignedVerticallyLeft(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineLeftY: Int = location.getX()
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { s: Any? -> true }
        Arrays.stream(layoutComponents).forEach({ c: LayoutComponent -> combinedPredicate.and(Predicate { r: Any? -> (baseLineLeftY == c.location.getX()) }) })
        return FinishValidationBuilder(combinedPredicate,
                { buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineLeftY, LayoutOptions.ALIGNED_VERTICALLY_LEFT) },
                { buildFailedAlignValidationMessage(comparingComponentsNames, baseLineLeftY, LayoutOptions.ALIGNED_VERTICALLY_LEFT) })
    }

    fun alignedHorizontallyAll(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineTopY: Int = location.getY()
        val baseLineBottomY: Int = location.getY() + size.getHeight()
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { s: Any? -> true }
        Arrays.stream(layoutComponents).forEach({ c: LayoutComponent ->
            val topY: Int = c.location.getY()
            val bottomY: Int = c.location.getY() + c.size.getHeight()
            combinedPredicate.and({ r: Any? -> (baseLineTopY == topY) }).and({ r: Any? -> (baseLineBottomY == bottomY) })
        })
        return FinishValidationBuilder(combinedPredicate,
                {
                    buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineTopY, LayoutOptions.ALIGNED_HORIZONTALLY_TOP) +
                            buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineBottomY, LayoutOptions.ALIGNED_HORIZONTALLY_BOTTOM)
                },
                {
                    buildFailedAlignValidationMessage(comparingComponentsNames, baseLineTopY, LayoutOptions.ALIGNED_HORIZONTALLY_TOP) +
                            buildFailedAlignValidationMessage(comparingComponentsNames, baseLineBottomY, LayoutOptions.ALIGNED_HORIZONTALLY_BOTTOM)
                })
    }

    fun alignedHorizontallyCentered(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineTopY: Int = location.getY() + size.getHeight() / 2
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { s: Any? -> true }
        Arrays.stream(layoutComponents).forEach({ c: LayoutComponent ->
            val bottomY: Int = c.location.getY() + c.size.getHeight() / 2
            combinedPredicate.and({ r: Any? -> (baseLineTopY == bottomY) })
        })
        return FinishValidationBuilder(combinedPredicate,
                { buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineTopY, LayoutOptions.ALIGNED_HORIZONTALLY_CENTERED) },
                { buildFailedAlignValidationMessage(comparingComponentsNames, baseLineTopY, LayoutOptions.ALIGNED_HORIZONTALLY_CENTERED) })
    }

    fun alignedHorizontallyTop(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineTopY: Int = location.getY()
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { s: Any? -> true }
        Arrays.stream(layoutComponents).forEach({ c: LayoutComponent -> combinedPredicate.and(Predicate { r: Any? -> (baseLineTopY == c.location.getY()) }) })
        return FinishValidationBuilder(combinedPredicate,
                { buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineTopY, LayoutOptions.ALIGNED_HORIZONTALLY_TOP) },
                { buildFailedAlignValidationMessage(comparingComponentsNames, baseLineTopY, LayoutOptions.ALIGNED_HORIZONTALLY_TOP) })
    }

    fun alignedHorizontallyBottom(vararg layoutComponents: LayoutComponent): FinishValidationBuilder {
        val baseLineBottomY: Int = location.getY() + size.getHeight()
        val comparingComponentsNames: String = getLayoutComponentsNames(layoutComponents)
        val combinedPredicate: Predicate<Boolean> = Predicate { true }
        Arrays.stream(layoutComponents).forEach({ c: LayoutComponent ->
            val bottomY: Int = c.location.getY() + c.size.getHeight()
            combinedPredicate.and({ r: Any? -> (baseLineBottomY == bottomY) })
        })
        return FinishValidationBuilder(combinedPredicate,
                { buildNotificationAlignValidationMessage(comparingComponentsNames, baseLineBottomY, LayoutOptions.ALIGNED_HORIZONTALLY_BOTTOM) },
                { buildFailedAlignValidationMessage(comparingComponentsNames, baseLineBottomY, LayoutOptions.ALIGNED_HORIZONTALLY_BOTTOM) })
    }

    fun height(): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(size.getHeight().toDouble())
    }

    fun width(): LayoutPreciseValidationBuilder {
        return LayoutPreciseValidationBuilder(size.getHeight().toDouble())
    }

    private fun getLayoutComponentsNames(layoutComponents: Array<out LayoutComponent>): String {
        val comparingComponentsNames: String = Arrays.stream(layoutComponents).skip(0).map({ x: LayoutComponent -> x.componentName }).collect(Collectors.joining(","))
        return comparingComponentsNames
    }

    private fun buildNotificationAlignValidationMessage(componentNames: String, expected: Int, validationType: LayoutOptions): String {
        return String.format("validate %s is %s %s %d px ", componentName, validationType, componentNames, expected)
    }

    private fun buildFailedAlignValidationMessage(componentNames: String, expected: Int, validationType: LayoutOptions): String {
        return String.format("%s should be %s %s %d px but was not. ", componentName, validationType, componentNames, expected)
    }

    private fun buildNotificationValidationMessage(secondLayoutComponent: LayoutComponent, validationType: LayoutOptions): String {
        return String.format("validate %s is %s of %s ", componentName, validationType, secondLayoutComponent.componentName)
    }

    private fun buildFailedValidationMessage(secondLayoutComponent: LayoutComponent, validationType: LayoutOptions): String {
        return String.format("%s should be %s of %s ", componentName, validationType, secondLayoutComponent.componentName)
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
        return ((outerComponent.location.getY() + outerComponent.size.getHeight()) - (innerComponent.location.getY() + innerComponent.size.getHeight())).toDouble()
    }

    private fun calculateLeftInsideOfDistance(innerComponent: LayoutComponent, outerComponent: LayoutComponent): Double {
        return (innerComponent.location.getX() - outerComponent.location.getX()).toDouble()
    }

    private fun calculateRightInsideOfDistance(innerComponent: LayoutComponent, outerComponent: LayoutComponent): Double {
        return ((outerComponent.location.getX() + outerComponent.size.getWidth()) - (innerComponent.location.getX() + innerComponent.size.getWidth())).toDouble()
    }
}