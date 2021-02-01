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
        val waitForAjaxTimeout: Int = 0,
        val waitUntilReadyTimeout: Int = 0,
        val waitForJavaScriptAnimationsTimeout: Int = 0,
        val waitForAngularTimeout: Int = 0,
        val waitForPartialUrl: Int = 0,
        val sleepInterval: Int = 0,
        val validationsTimeout: Int = 0,
        val elementToBeVisibleTimeout: Int = 0,
        val elementToExistTimeout: Int = 0,
        val elementToNotExistTimeout: Int = 0,
        val elementToBeClickableTimeout: Int = 0,
        val elementNotToBeVisibleTimeout: Int = 0,
        val elementToHaveContentTimeout: Int = 0,
)