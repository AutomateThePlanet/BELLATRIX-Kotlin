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
package solutions.bellatrix.desktop.configuration

import lombok.Getter
import lombok.Setter

data class TimeoutSettings(
        val sleepInterval: Long = 30,
        val validationsTimeout: Long = 30,
        val elementToBeVisibleTimeout: Long = 30,
        val elementToExistTimeout: Long = 30,
        val elementToNotExistTimeout: Long = 30,
        val elementToBeClickableTimeout: Long = 30,
        val elementNotToBeVisibleTimeout: Long = 30,
        val elementToHaveContentTimeout: Long = 30,
)