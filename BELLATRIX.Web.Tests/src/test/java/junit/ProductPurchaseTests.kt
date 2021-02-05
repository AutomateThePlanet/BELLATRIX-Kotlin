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

package junit

import org.junit.jupiter.api.Test
import pages.cartpage.CartPage
import pages.checkoutpage.CheckoutPage
import pages.checkoutpage.PurchaseInfo
import pages.mainpage.MainPage
import solutions.bellatrix.components.*
import solutions.bellatrix.infrastructure.*
import solutions.bellatrix.infrastructure.junit.WebTest
import solutions.bellatrix.validations.*

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
class ProductPurchaseTests : WebTest() {
    @Test
    fun completePurchaseSuccessfully_first() {
        app.navigate.to("http://demos.bellatrix.solutions/")
        val addToCartFalcon9 = app.create.byCss<Anchor>("[data-product_id*='28']")
        val blogLink = app.create.byCss<Anchor>("[data-product_id*='28']")
        addToCartFalcon9.click()
        blogLink.layout().assertAboveOf(addToCartFalcon9, 9.0)
//        blogLink.layout() assertAboveOf addToCartFalcon9
        MainPage.asserts.productBoxLink("", "")
        blogLink.validateHrefIs("http://demos.bellatrix.solutions/")
    }

    @Test
    fun completePurchaseSuccessfully_second() {
        app.navigate.to("http://demos.bellatrix.solutions/")
        val addToCartFalcon9 = app.create.byCss<Anchor>("[data-product_id*='28']")
        addToCartFalcon9.click()
    }

    @Test
    fun falcon9LinkAddsCorrectProduct() {
        MainPage.open()
        MainPage.asserts.productBoxLink("Falcon 9", "http://demos.bellatrix.solutions/product/falcon-9/")
    }

    @Test
    fun saturnVLinkAddsCorrectProduct() {
        MainPage.open()
        MainPage.asserts.productBoxLink("Saturn V", "http://demos.bellatrix.solutions/product/saturn-v/")
    }

    @Test
    fun purchaseFalcon9WithoutFacade() {
        MainPage.open()
        MainPage.addRocketToShoppingCart("Falcon 9")
        CartPage.applyCoupon("happybirthday")
        CartPage.asserts.couponAppliedSuccessfully()
        CartPage.increaseProductQuantity(2)
        CartPage.asserts.totalPrice("114.00€")
        CartPage.clickProceedToCheckout()
        CheckoutPage.fillInfo(PurchaseInfo())
        CheckoutPage.asserts.orderReceived()
    }

    @Test
    fun purchaseSaturnVWithoutFacade() {
        MainPage.open()
        MainPage.addRocketToShoppingCart("Saturn V")
        CartPage.applyCoupon("happybirthday")
        CartPage.asserts.couponAppliedSuccessfully()
        CartPage.increaseProductQuantity(3)
        CartPage.asserts.totalPrice("355.00€")
        CartPage.clickProceedToCheckout()
        CheckoutPage.fillInfo(PurchaseInfo())
        CheckoutPage.asserts.orderReceived()
    }
}