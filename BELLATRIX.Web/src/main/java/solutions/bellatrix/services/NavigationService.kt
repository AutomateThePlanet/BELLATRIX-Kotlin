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

import solutions.bellatrix.configuration.ConfigurationService
import solutions.bellatrix.configuration.WebSettings
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.WebDriver
import java.lang.Exception

object NavigationService : WebService() {
    // TODO: UrlNotNavigatedEvent
    fun to(url: String?) {
        wrappedDriver.navigate().to(url)
    }

    fun NavigateToLocalPage(filePath: String?) {
//        var assembly = Assembly.GetExecutingAssembly();
//        string path = Path.GetDirectoryName(assembly.Location);
//
//        string pageFilePath = Path.Combine(path ?? throw new InvalidOperationException(), filePath);
//
//        if (WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.Safari) || WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.Firefox) || WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.FirefoxHeadless))
//        {
//            pageFilePath = string.Concat("file:///", pageFilePath);
//        }

//        if (RuntimeInformation.IsOSPlatform(OSPlatform.OSX))
//        {
//            pageFilePath = pageFilePath.Replace('\\', '/').Replace("file:////", "file://////").Replace(" ", "%20");
//        }
//
//        if (!WrappedWebDriverCreateService.BrowserConfiguration.BrowserType.Equals(BrowserType.Safari))
//        {
//            Navigate(new Uri(pageFilePath, uriKind: UriKind.Absolute));
//        }
//        else
//        {
//            Navigate(pageFilePath);
//        }
    }

    fun waitForPartialUrl(partialUrl: String?) {
        try {
            val waitForPartialTimeout: Int = ConfigurationService.get<WebSettings>().timeoutSettings.waitForPartialUrl
            val sleepInterval: Int = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
            val webDriverWait = WebDriverWait(wrappedDriver, waitForPartialTimeout.toLong(), sleepInterval.toLong())
            webDriverWait.until { d: WebDriver? -> wrappedDriver.currentUrl.contains(partialUrl!!) }
        } catch (ex: Exception) {
//            UrlNotNavigatedEvent?.Invoke(this, new UrlNotNavigatedEventArgs(ex));
            throw ex
        }
    }

//    @Throws(MalformedURLException::class)
//    fun getQueryParameter(parameterName: String): List<String> {
//        return splitQuery(wrappedDriver.currentUrl)[parameterName]!!
//    }
//
//    @Throws(MalformedURLException::class)
//    private fun splitQuery(url: String): Map<String, List<String>> {
//        return if (Strings.isNullOrEmpty(URL(url).query)) {
//            emptyMap()
//        } else Arrays.stream(URL(url).query.split("&".toRegex()).toTypedArray())
//                .map { it: String ->
//                    try {
//                        return@map splitQueryParameter(it)
//                    } catch (e: UnsupportedEncodingException) {
//                        e.printStackTrace()
//                    }
//                    null
//                }
//                .collect(Collectors.groupingBy<SimpleImmutableEntry<String, String>?, String, List<String>, Any, LinkedHashMap<String, List<String>>>(Function<SimpleImmutableEntry<String, String>?, String> { SimpleImmutableEntry. }, Supplier { LinkedHashMap() }, Collectors.mapping<SimpleImmutableEntry<String, String>?, String, Any, List<String>>(Function<SimpleImmutableEntry<String, String>?, String> { java.util.Map.Entry.value }, Collectors.toList())))
//    }
//
//    @Throws(UnsupportedEncodingException::class)
//    private fun splitQueryParameter(it: String): SimpleImmutableEntry<String, String> {
//        val idx = it.indexOf("=")
//        val key = if (idx > 0) it.substring(0, idx) else it
//        val value = if (idx > 0 && it.length > idx + 1) it.substring(idx + 1) else null
//        return SimpleImmutableEntry(
//                URLDecoder.decode(key, "UTF-8"),
//                URLDecoder.decode(value, "UTF-8")
//        )
//    }
}