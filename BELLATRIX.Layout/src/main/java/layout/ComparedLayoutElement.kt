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

class ComparedLayoutElement (val layoutComponent: LayoutComponent, val layoutAssertion: LayoutAssertion) {
    infix fun ComparedLayoutElement.eq(expected: Double) {
//        val actualDistance = calculateAboveOfDistance(component, this.layoutComponent)
//        Assert.assertEquals(expected, actualDistance, String.format("%s should be %d px above of %s but was %d px.", component.elementName, expected, this.layoutComponent.elementName, actualDistance))
//        LayoutAssertionsFactory.ASSERTED_ABOVE_OF.broadcast(LayoutTwoComponentsActionEventArgs(component, this.layoutComponent, expected.toString()))
    }

    infix fun ComparedLayoutElement.ne(expected: Double) {  }
    infix fun ComparedLayoutElement.lt(expected: Double) {  }
    infix fun ComparedLayoutElement.le(expected: Double) {  }
    infix fun ComparedLayoutElement.gt(expected: Double) {  }
    infix fun ComparedLayoutElement.ge(expected: Double) {  }
}