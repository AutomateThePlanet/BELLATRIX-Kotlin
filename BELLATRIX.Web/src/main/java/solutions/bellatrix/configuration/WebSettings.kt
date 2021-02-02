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
class WebSettings(
        var baseUrl: String = "",
        var chrome: BrowserSettings = BrowserSettings(30, 2),
        var firefox: BrowserSettings = BrowserSettings(30, 2),
        var edge: BrowserSettings = BrowserSettings(30, 2),
        var timeoutSettings: TimeoutSettings = TimeoutSettings(),
        val elementWaitTimeout: Int = 0,
        val automaticallyScrollToVisible: Boolean = false,
        val waitUntilReadyOnElementFound: Boolean = false,
        val waitForAngular: Boolean = false,
        val artificialDelayBeforeAction: Int = 0,
)