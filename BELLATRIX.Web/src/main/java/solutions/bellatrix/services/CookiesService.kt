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
    fun add(cookieName: String?, cookieValue: String?, path: String?) {
        wrappedDriver.manage().addCookie(Cookie(cookieName, cookieValue, path))
    }

    fun add(cookieToAdd: Cookie?) {
        wrappedDriver.manage().addCookie(cookieToAdd)
    }

    fun deleteAll() {
        wrappedDriver.manage().deleteAllCookies()
    }

    fun delete(cookieName: String?) {
        wrappedDriver.manage().deleteCookieNamed(cookieName)
    }

    val all = wrappedDriver.manage().cookies

    fun get(cookieName: String?): Cookie {
        return wrappedDriver.manage().getCookieNamed(cookieName)
    }
}