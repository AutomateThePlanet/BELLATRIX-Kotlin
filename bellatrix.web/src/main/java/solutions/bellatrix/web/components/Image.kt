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
package solutions.bellatrix.web.components

import solutions.bellatrix.web.components.contracts.*

open class Image : WebComponent(), ComponentSrc, ComponentHeight, ComponentWidth, ComponentLongDesc, ComponentAlt, ComponentSrcSet, ComponentSizes {
    override val componentClass: Class<*>
        get() = javaClass

    override val src: String
        get() = defaultGetSrcAttribute()

    override val longdesc: String
        get() = defaultGetLongDescAttribute()

    override val alt: String
        get() = defaultGetAltAttribute()

    override val srcset: String
        get() = defaultGetSrcSetAttribute()

    override val sizes: String
        get() = defaultGetSizesSetAttribute()

    override val height: Int?
        get() = defaultGetHeightAttribute()

    override val width: Int?
        get() = defaultGetWidthAttribute()
}