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
package pages.checkoutpage

import solutions.bellatrix.pages.WebPage

class CheckoutPage : WebPage<Map, Asserts>() {
    override val url: String = "http://demos.bellatrix.solutions/checkout/"
    override val map: Map
        get() = Map()
    override val asserts: Asserts
        get() = Asserts(map)

    fun fillInfo(purchaseInfo: PurchaseInfo) {
        map.firstName.setText(purchaseInfo.firstName)
        map.lastName.setText(purchaseInfo.lastName)
        map.company.setText(purchaseInfo.company)
        map.countryWrapper.click()
        map.countryFilter.setText(purchaseInfo.country)
        map.countryOption(purchaseInfo.country).click()
        map.address1.setText(purchaseInfo.address1)
        map.address2.setText(purchaseInfo.address2)
        map.city.setText(purchaseInfo.city)
        map.zip.setText(purchaseInfo.zip)
        map.phone.setText(purchaseInfo.phone)
        map.email.setText(purchaseInfo.email)

        if (purchaseInfo.shouldCreateAccount) map.createAccountCheckBox.check()
        if (purchaseInfo.shouldCheckPayment) map.checkPaymentsRadioButton.click()

        map.placeOrderButton.click()
        browser.waitForAjax()
    }
}