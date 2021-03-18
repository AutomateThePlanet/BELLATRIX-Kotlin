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
package solutions.bellatrix.android.components

import solutions.bellatrix.android.findstrategies.ClassFindStrategy

class RadioGroup : AndroidComponent() {
    override val componentClass: Class<*>
        get() = javaClass

    fun clickByText(text: String) {
        val allRadioButtons = getAll()
        for (radioButton in allRadioButtons) {
            if (radioButton.text == text) {
                radioButton.click()
                break
            }
        }
    }

    fun clickByIndex(index: Int) {
        val allRadioButtons = getAll()
        if (index > allRadioButtons.size - 1) throw IllegalArgumentException("Only ${allRadioButtons.size} radio buttons were present which is less than the specified index = $index.")
        for ((currentIndex, radioButton) in allRadioButtons.withIndex()) {
            if (currentIndex == index) {
                radioButton.click()
                break
            }
        }
    }

    fun getChecked(): RadioButton {
        return createByXPath("//*[@checked='true']")
    }

    fun getAll(): List<RadioButton> {
        return createAll<RadioButton, ClassFindStrategy>("android.widget.RadioButton")
    }
}