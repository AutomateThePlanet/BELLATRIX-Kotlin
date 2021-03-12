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
package solutions.bellatrix.web.infrastructure

import org.apache.commons.lang3.StringUtils
import solutions.bellatrix.core.utilities.ResourcesReader.getFileAsString
import solutions.bellatrix.core.utilities.TempFileWriter.writeStringToTempFile
import java.io.File
import solutions.bellatrix.core.configuration.ConfigurationService
import solutions.bellatrix.web.configuration.WebSettings
import org.openqa.selenium.support.ui.WebDriverWait
import java.lang.IllegalArgumentException
import org.openqa.selenium.WebDriver
import solutions.bellatrix.web.services.JavaScriptService
import java.lang.Boolean

object FullPageScreenshotEngine {
    private const val GENERATE_SCREENSHOT_JS = "function genScreenshot () {var canvasImgContentDecoded; html2canvas(document.body).then(function(canvas) { window.canvasImgContentDecoded = canvas.toDataURL(\"image/png\"); });}genScreenshot();"
    @JvmStatic
    fun takeScreenshot(): File {
        val html2CanvasContent = getFileAsString(FullPageScreenshotEngine::class.java, "html2canvas.js")
        val timeoutInterval = ConfigurationService.get<WebSettings>().timeoutSettings.scriptTimeout
        val sleepInterval = ConfigurationService.get<WebSettings>().timeoutSettings.sleepInterval
        val webDriverWait = WebDriverWait(DriverService.wrappedDriver(), timeoutInterval, sleepInterval)
        webDriverWait.ignoring(IllegalArgumentException::class.java)
        JavaScriptService.execute(html2CanvasContent)
        JavaScriptService.execute(GENERATE_SCREENSHOT_JS)
        webDriverWait.until { wd: WebDriver ->
            val response = JavaScriptService.execute("return (typeof canvasImgContentDecoded === 'undefined' || canvasImgContentDecoded === null)") as String
            if (StringUtils.isEmpty(response)) return@until false
            Boolean.parseBoolean(response)
        }
        webDriverWait.until { wd: WebDriver -> !StringUtils.isEmpty(JavaScriptService.execute("return canvasImgContentDecoded;") as String) }
        var pngContent = JavaScriptService.execute("return canvasImgContentDecoded;") as String
        pngContent = pngContent.replace("data:image/png;base64,", "")
        return writeStringToTempFile(pngContent)
    }
}