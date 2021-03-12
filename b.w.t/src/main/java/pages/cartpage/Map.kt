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
package pages.cartpage

import solutions.bellatrix.web.components.*
import solutions.bellatrix.web.pages.PageMap

class Map : PageMap() {
    val couponCodeTextField: TextField = create.byId("coupon_code")
    val applyCouponButton: Button = create.byCss("[value*='Apply coupon']")
    val quantityBox: TextField = create.byCss("[class*='input-text qty text']")
    val updateCart: Button = create.byCss("[value*='Update cart']")
    val totalSpan: Div = create.byCss("[class*='woocommerce-message']")
    val messageAlert: Span = create.byXPath("//*[@class='order-total']//span")
    val proceedToCheckout: Button = create.byXPath("[class*='checkout-button button alt wc-forward']")
}