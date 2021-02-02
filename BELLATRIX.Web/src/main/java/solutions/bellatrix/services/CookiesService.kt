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
package solutions.bellatrix.services

import org.openqa.selenium.Cookie

object CookiesService : WebService() {
    fun addCookie(cookieName: String?, cookieValue: String?, path: String?) {
        wrappedDriver.manage().addCookie(Cookie(cookieName, cookieValue, path))
    }

    fun addCookie(cookieToAdd: Cookie?) {
        wrappedDriver.manage().addCookie(cookieToAdd)
    }

    fun deleteAllCookies() {
        wrappedDriver.manage().deleteAllCookies()
    }

    fun deleteCookie(cookieName: String?) {
        wrappedDriver.manage().deleteCookieNamed(cookieName)
    }

    fun GetAllCookies(): Set<Cookie> {
        return wrappedDriver.manage().cookies
    }

    fun GetCookie(cookieName: String?): Cookie {
        return wrappedDriver.manage().getCookieNamed(cookieName)
    }
}