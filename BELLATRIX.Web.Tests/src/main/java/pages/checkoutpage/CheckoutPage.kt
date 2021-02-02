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

class CheckoutPage : WebPage<Components, Asserts>() {
    override val url: String = "http://demos.bellatrix.solutions/checkout/"
    override val components: Components
        get() = Components()
    override val asserts: Asserts
        get() = Asserts(components)

    fun fillInfo(purchaseInfo: PurchaseInfo) {
        components.firstName.setText(purchaseInfo.firstName)
        components.lastName.setText(purchaseInfo.lastName)
        components.company.setText(purchaseInfo.company)
        components.countryWrapper.click()
        components.countryFilter.setText(purchaseInfo.country)
        components.countryOption(purchaseInfo.country).click()
        components.address1.setText(purchaseInfo.address1)
        components.address2.setText(purchaseInfo.address2)
        components.city.setText(purchaseInfo.city)
        components.zip.setText(purchaseInfo.zip)
        components.phone.setText(purchaseInfo.phone)
        components.email.setText(purchaseInfo.email)

        if (purchaseInfo.shouldCreateAccount) components.createAccountCheckBox.check()
        if (purchaseInfo.shouldCheckPayment) components.checkPaymentsRadioButton.click()

        components.placeOrderButton.click()
        browser.waitForAjax()
    }
}