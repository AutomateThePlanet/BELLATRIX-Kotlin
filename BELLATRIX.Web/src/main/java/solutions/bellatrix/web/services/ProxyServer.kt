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
package solutions.bellatrix.web.services

import net.lightbody.bmp.core.har.HarEntry
import java.lang.ThreadLocal
import net.lightbody.bmp.BrowserMobProxyServer
import org.testng.Assert
import java.io.IOException
import java.net.ServerSocket

class ProxyServer {
    companion object {
        private val proxyServer = ThreadLocal<BrowserMobProxyServer>()
        fun init(): Int {
            val port: Int = findFreePort()
            proxyServer.set(BrowserMobProxyServer())
            proxyServer.get().start(port)
            return port
        }

        fun close() {
            if (proxyServer.get() != null) {
                proxyServer.get()!!.stop()
            }
        }

        private fun findFreePort(): Int {
            var port = 0
            // For ServerSocket port number 0 means that the port number is automatically allocated.
            // Disable timeout and reuse address after closing the socket.
            try {
                ServerSocket(0).use { socket ->
                    socket.reuseAddress = true
                    port = socket.localPort
                }
            } catch (ignored: IOException) {}

            if (port > 0) port

            throw RuntimeException("Could not find a free port")
        }
    }

    fun assertNoErrorCodes() {
        val harEntries = proxyServer.get()!!.har.log.entries
        val areThereErrorCodes = harEntries.stream().anyMatch { r: HarEntry ->
            (r.response.status > 400
                    && r.response.status < 599)
        }
        Assert.assertFalse(areThereErrorCodes)
    }

    fun assertRequestMade(url: String?) {
        val harEntries = proxyServer.get()!!.har.log.entries
        val areRequestsMade = harEntries.stream().anyMatch { r: HarEntry -> r.request.url.contains(url!!) }
        Assert.assertTrue(areRequestsMade)
    }

    fun assertNoLargeImagesRequested() {
        val harEntries = proxyServer.get()!!.har.log.entries
        val areThereLargeImages = harEntries.stream().anyMatch { r: HarEntry ->
            (r.response.content.mimeType.startsWith("image")
                    && r.response.content.size > 40000)
        }
        Assert.assertFalse(areThereLargeImages)
    }
}