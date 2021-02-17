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
package pages.mainmenusection

import solutions.bellatrix.components.Anchor
import solutions.bellatrix.pages.PageMap

class Map : PageMap() {
    val homeLink: Anchor = create.byLinkText("Home")
    val blogLink: Anchor = create.byLinkText("Blog")
    val cartLink: Anchor = create.byLinkText("Cart")
    val checkoutLink: Anchor = create.byLinkText("Checkout")
    val myAccountLink: Anchor = create.byLinkText("My Account")
    val promotionsLink: Anchor = create.byLinkText("Promotions")
}