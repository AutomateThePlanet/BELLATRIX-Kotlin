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
package solutions.bellatrix.android.services

import java.io.File

object FileSystemService : MobileService() {
    fun PullFile(pathOnDevice: String?): ByteArray {
        return wrappedAndroidDriver.pullFile(pathOnDevice)
    }

    fun pullFolder(remotePath: String?): ByteArray {
        return wrappedAndroidDriver.pullFolder(remotePath)
    }

    fun pushFile(pathOnDevice: String?, file: File?) {
        wrappedAndroidDriver.pushFile(pathOnDevice, file)
    }

    fun pushFile(pathOnDevice: String?, base64Data: ByteArray?) {
        wrappedAndroidDriver.pushFile(pathOnDevice, base64Data)
    }
}