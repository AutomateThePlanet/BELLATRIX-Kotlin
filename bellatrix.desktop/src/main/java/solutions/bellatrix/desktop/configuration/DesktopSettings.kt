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
class DesktopSettings {
    var executionType: String = "regular"
    var defaultLifeCycle: String = "restart everytime"
    var defaultAppPath: String = "user.home"
    var downloadDemoApps: Boolean = false
    var serviceUrl: String = "http://127.0.0.1:4722"
    var timeoutSettings: TimeoutSettings = TimeoutSettings()
    var gridSettings: List<GridSettings> = listOf()
    val automaticallyScrollToVisible: Boolean = false
    val artificialDelayBeforeAction: Int = 0
    val shouldHighlightElements: Boolean = true
    val shouldCaptureHttpTraffic: Boolean = true
    val screenshotsOnFailEnabled: Boolean = true
    val screenshotsSaveLocation: String = "user.home\\BELLATRIX\\Screenshots"
    val videosOnFailEnabled: Boolean = true
    val videosSaveLocation: String = "user.home\\BELLATRIX\\Videos"
}