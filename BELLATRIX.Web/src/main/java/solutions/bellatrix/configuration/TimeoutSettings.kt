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
package solutions.bellatrix.configuration

data class TimeoutSettings(
        val waitForAjaxTimeout: Int = 30,
        val waitUntilReadyTimeout: Int = 30,
        val waitForJavaScriptAnimationsTimeout: Int = 30,
        val waitForAngularTimeout: Int = 30,
        val waitForPartialUrl: Int = 30,
        val sleepInterval: Int = 30,
        val validationsTimeout: Int = 30,
        val elementToBeVisibleTimeout: Int = 30,
        val elementToExistTimeout: Int = 30,
        val elementToNotExistTimeout: Int = 30,
        val elementToBeClickableTimeout: Int = 30,
        val elementNotToBeVisibleTimeout: Int = 30,
        val elementToHaveContentTimeout: Int = 30,
)