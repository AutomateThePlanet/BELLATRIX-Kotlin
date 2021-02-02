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
package solutions.bellatrix.services

import solutions.bellatrix.infrastructure.DriverService.wrappedDriver
import solutions.bellatrix.components.WebComponent
import solutions.bellatrix.waitstrategies.WaitStrategy

object ComponentWaitService : WebService() {
    fun wait(component: WebComponent, waitStrategy: WaitStrategy) {
        if (component.parentWrappedElement == null) {
            waitStrategy.waitUntil(wrappedDriver(), component.findStrategy.convert())
        } else {
            waitStrategy.waitUntil(component.parentWrappedElement, component.findStrategy.convert())
        }
    }
}