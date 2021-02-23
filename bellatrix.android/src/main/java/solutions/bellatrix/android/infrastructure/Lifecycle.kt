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
package solutions.bellatrix.android.infrastructure

import java.util.*

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
 */   enum class Lifecycle(val text: String) {
    NOT_SET("not_set"), REUSE_IF_STARTED("reuse_if_started"), RESTART_EVERY_TIME("restart_every_time"), RESTART_ON_FAIL("restart_on_fail");

    companion object {
        fun fromText(text: String?): Lifecycle {
            return Arrays.stream(values())
                    .filter { l: Lifecycle ->
                        l.text.equals(text, ignoreCase = true) ||
                                l.text.replace("_", " ").equals(text, ignoreCase = true) ||
                                l.text.replace("_", "").equals(text, ignoreCase = true)
                    }
                    .findFirst().orElse(RESTART_EVERY_TIME)
        }
    }
}