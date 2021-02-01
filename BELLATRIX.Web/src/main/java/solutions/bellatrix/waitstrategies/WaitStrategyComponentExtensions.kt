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
package solutions.bellatrix.waitstrategies

import solutions.bellatrix.components.WebComponent

class WaitStrategyComponentExtensions {
    fun <TComponent : WebComponent> TComponent.exists(timeoutInterval: Int = 30, sleepInterval: Int = 2): TComponent {
        this.ensureState(ToExistsWaitStrategy(timeoutInterval, sleepInterval))
        return this;
    }

    fun <TComponent : WebComponent> TComponent.beVisible(timeoutInterval: Int = 30, sleepInterval: Int = 2): TComponent {
        this.ensureState(ToBeVisibleWaitStrategy(timeoutInterval, sleepInterval))
        return this;
    }

    fun<TComponent : WebComponent> TComponent. beClickable(timeoutInterval: Int = 30, sleepInterval: Int = 2): TComponent {
        this.ensureState(ToBeClickableWaitStrategy(timeoutInterval, sleepInterval))
        return this;
    }
}