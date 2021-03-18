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
package solutions.bellatrix.android.waitstrategies

class WaitStrategyFactory {
    fun exists(): ToExistsWaitStrategy {
        return ToExistsWaitStrategy(30, 2)
    }

    fun exists(timeoutInterval: Int, sleepInterval: Int): ToExistsWaitStrategy {
        return ToExistsWaitStrategy(timeoutInterval.toLong(), sleepInterval.toLong())
    }

    fun beVisible(timeoutInterval: Int, sleepInterval: Int): ToBeVisibleWaitStrategy {
        return ToBeVisibleWaitStrategy(timeoutInterval.toLong(), sleepInterval.toLong())
    }

    fun beVisible(): ToBeVisibleWaitStrategy {
        return ToBeVisibleWaitStrategy(30, 2)
    }

    fun beClickable(timeoutInterval: Int, sleepInterval: Int): ToBeClickableWaitStrategy {
        return ToBeClickableWaitStrategy(timeoutInterval.toLong(), sleepInterval.toLong())
    }

    fun beClickable(): ToBeClickableWaitStrategy {
        return ToBeClickableWaitStrategy(30, 2)
    }
}