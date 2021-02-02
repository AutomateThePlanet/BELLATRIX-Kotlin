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

import solutions.bellatrix.components.TextField
import solutions.bellatrix.pages.WebPage

class CartPage : WebPage<Components, Asserts>() {
    override val url: String = "http://demos.bellatrix.solutions/cart/"

    override fun waitForPageLoad() {
        components.couponCodeTextField.toExists<TextField>().waitToBe()
    }

    override val components: Components
        get() = Components()
    override val asserts: Asserts
        get() = Asserts(components)

    val total: String = components.totalSpan.text
    val messageNotification: String = components.messageAlert.text

    fun applyCoupon(coupon: String?) {
        components.couponCodeTextField.setText(coupon)
        components.applyCouponButton.click()
        browser.waitForAjax()
    }

    fun increaseProductQuantity(newQuantity: Int) {
        components.quantityBox.setText(newQuantity.toString())
        components.updateCart.click()
        browser.waitForAjax()
    }

    fun clickProceedToCheckout() {
        components.proceedToCheckout.click()
        browser.waitUntilPageLoadsCompletely()
    }
}