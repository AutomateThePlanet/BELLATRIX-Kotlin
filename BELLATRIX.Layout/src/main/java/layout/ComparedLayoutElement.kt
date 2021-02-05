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

class ComparedLayoutElement (internal val firstLayoutComponent: LayoutComponent, internal val secondLayoutComponent: LayoutComponent, internal val layoutAssertion: LayoutAssertion) {
    fun EQ(expected: Double) : AssertedComparedLayoutElement {
//        val actualDistance = calculateAboveOfDistance(component, this.layoutComponent)
//        Assert.assertEquals(expected, actualDistance, String.format("%s should be %d px above of %s but was %d px.", component.elementName, expected, this.layoutComponent.elementName, actualDistance))
//        LayoutAssertionsFactory.ASSERTED_ABOVE_OF.broadcast(LayoutTwoComponentsActionEventArgs(component, this.layoutComponent, expected.toString()))
        return this;
    }
    fun NE(expected: Int) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected.toDouble());
    }

    fun LT(expected: Int) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected.toDouble());
    }
    fun LE(expected: Int) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected.toDouble());
    }

    fun GT(expected: Int) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected.toDouble());
    }

    fun GE(expected: Int): AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected.toDouble());
    }

    fun NE(expected: Double) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected);
    }

    fun LT(expected: Double) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected);
    }
    fun LE(expected: Double) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected);
    }

    fun GT(expected: Double) : AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected);
    }

    fun GE(expected: Double): AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected);
    }

    fun AP(expected: Double, percent: Double): AssertedComparedLayoutElement {
        return AssertedComparedLayoutElement(this.firstLayoutComponent, this.secondLayoutComponent, this.layoutAssertion, expected);
    }

    fun assert() {
    }
//    infix fun ComparedLayoutElement.ne(expected: Double) {  }
//    infix fun ComparedLayoutElement.lt(expected: Double) {  }
//    infix fun ComparedLayoutElement.le(expected: Double) {  }
//    infix fun ComparedLayoutElement.gt(expected: Double) {  }
//    infix fun ComparedLayoutElement.ge(expected: Double) {  }
}