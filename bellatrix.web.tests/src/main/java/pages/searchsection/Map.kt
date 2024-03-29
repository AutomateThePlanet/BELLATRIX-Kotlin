/*
 * Copyright 2021 Automate The Planet Ltd.
 * Author: Teodor Nikolov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pages.searchsection

import solutions.bellatrix.web.components.Anchor
import solutions.bellatrix.web.components.Button
import solutions.bellatrix.web.components.TextField
import solutions.bellatrix.web.pages.PageMap

class Map : PageMap() {
    val searchField: TextField = create.byId("woocommerce-product-search-field-0")
}