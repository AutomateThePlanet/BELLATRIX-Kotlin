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
package plugins.video

import org.apache.commons.io.FilenameUtils
import solutions.bellatrix.utilities.FileDownloader
import solutions.bellatrix.utilities.RuntimeInformation
import java.lang.Exception
import java.nio.file.Paths

class FFmpegVideoRecorder : AutoCloseable {
    override fun close() {
        try {
            if (RuntimeInformation.IS_WINDOWS) {
                Runtime.getRuntime().exec("taskkill /IM ffmpeg_windows.exe /F")
            } else if (RuntimeInformation.IS_MAC) {
                Runtime.getRuntime().exec("killall ffmpeg_osx")
            } else {
                Runtime.getRuntime().exec("killall ffmpeg_linux")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startRecording(videoSaveDir: String, videoFileName: String): String {
        val videoFullPath: String = Paths.get(videoSaveDir, videoFileName).toString()
        try {
            if (RuntimeInformation.IS_WINDOWS) {
                val recorderFile = FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.0/ffmpeg_windows.exe")
                val videoFilePathWithExtension = "${FilenameUtils.removeExtension(videoFullPath)}.mpg"
                Runtime.getRuntime().exec( "$recorderFile -f gdigrab -framerate 30 -i desktop $videoFilePathWithExtension")
            } else if (RuntimeInformation.IS_MAC) {
                val recorderFile = FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.0/ffmpeg_osx")
                val videoFilePathWithExtension = "${FilenameUtils.removeExtension(videoFullPath)}.mkv"
                Runtime.getRuntime().exec("$recorderFile -f avfoundation -framerate 10 -i \"0:0\" $videoFilePathWithExtension")
            } else {
                val recorderFile = FileDownloader.downloadToUsersFolder("https://github.com/AutomateThePlanet/BELLATRIX/releases/download/1.0/ffmpeg_linux")
                val videoFilePathWithExtension = "${FilenameUtils.removeExtension(videoFullPath)}.mp4"
                Runtime.getRuntime().exec( "$recorderFile -f x11grab -framerate 30 -i :0.0+100,200 $videoFilePathWithExtension")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return videoFullPath
    }
}