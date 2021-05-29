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

import solutions.bellatrix.web.components.TextField
import solutions.bellatrix.web.pages.WebPage

object CartPage : WebPage<Map, Asserts>() {
    override val url: String = "http://demos.bellatrix.solutions/cart/"

    override fun waitForPageLoad() {
        map.couponCodeTextField.toExist<TextField>().waitToBe()
    }

    override val map: Map
        get() = Map()
    override val asserts: Asserts
        get() = Asserts(map)

    fun applyCoupon(coupon: String) {
        map.couponCodeTextField.setText(coupon)
        map.applyCouponButton.click()
        browser.waitForAjax()
    }

    fun increaseProductQuantity(newQuantity: Int) {
        map.quantityBox.setText(newQuantity.toString())
        map.updateCart.click()
        browser.waitForAjax()
    }

    fun clickProceedToCheckout() {
        map.proceedToCheckout.click()
        browser.waitUntilPageLoadsCompletely()
    }
}