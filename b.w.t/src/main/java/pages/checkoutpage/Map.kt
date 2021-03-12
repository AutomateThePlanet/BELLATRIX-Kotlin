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

import solutions.bellatrix.web.components.*
import solutions.bellatrix.web.pages.PageMap

class Map : PageMap() {
    val firstName: TextField = create.byId("billing_first_name")
    val lastName: TextField = create.byId("billing_last_name")
    val company: TextField = create.byId("billing_company")
    val countryWrapper: Button = create.byId("select2-billing_country-container")
    val countryFilter: TextField = create.byClass("select2-search__field")
    val address1: TextField = create.byId("billing_address_1")
    val address2: TextField = create.byId( "billing_address_2")
    val city: TextField = create.byId( "billing_city")
    val zip: TextField = create.byId( "billing_postcode")
    val phone: TextField = create.byId( "billing_phone")
    val email: TextField = create.byId( "billing_email")
    val createAccountCheckBox: CheckBox = create.byId( "createaccount")
    val checkPaymentsRadioButton: RadioButton = create.byCss( "[for*='payment_method_cheque']")
    val placeOrderButton: Button = create.byId( "place_order")
    val receivedMessage: Heading = create.byXPath( "//h1")
    fun countryOption(countryName: String): Button = create.byXPath("//*[contains(text(),'$countryName')]")
}