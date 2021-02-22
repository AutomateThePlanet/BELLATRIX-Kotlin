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
package solutions.bellatrix.core.utilities

import java.nio.file.Paths
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

object FileDownloader {
    fun download(url: String, fullFilePath: String) {
        val file = Paths.get(fullFilePath).toFile()
        if (file.exists()) return
        val urlStream = URL(url).openStream()
        val readableByteChannel = Channels.newChannel(urlStream)
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
        urlStream.close()
        fileOutputStream.close()
    }

    fun downloadToUsersFolder(url: String): String {
        val fileName = url.split("/".toRegex()).last();
        val userHomeDir = System.getProperty("user.home")
        val file = Paths.get(userHomeDir, fileName).toFile()
        if (file.exists()) return file.path
        val urlStream = URL(url).openStream()
        val readableByteChannel = Channels.newChannel(urlStream)
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
        urlStream.close()
        fileOutputStream.close()
        return file.path
    }
}