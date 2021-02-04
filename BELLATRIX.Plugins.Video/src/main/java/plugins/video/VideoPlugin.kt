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

import org.apache.commons.io.FileUtils
import org.testng.ITestResult
import solutions.bellatrix.plugins.EventListener
import solutions.bellatrix.plugins.Plugin
import solutions.bellatrix.plugins.TestResult
import java.io.File
import java.lang.Exception
import java.lang.reflect.Method
import java.nio.file.Paths

abstract class VideoPlugin(private val isEnabled: Boolean) : Plugin() {
    protected abstract val outputFolder: String
    protected abstract fun getUniqueFileName(testName: String): String

    companion object {
        val VIDEO_GENERATED = EventListener<VideoPluginEventArgs>()
        val VIDEO_FULL_PATH = ThreadLocal<String>()
        val FFMPEG_VIDEO_RECORDER = FFmpegVideoRecorder()
    }

    override fun preBeforeTest(testResult: TestResult, memberInfo: Method) {
        if (isEnabled) {
            try {
                val videoSaveDir = outputFolder
                val videoFileName = getUniqueFileName(memberInfo.name)
                takeVideo(videoSaveDir, videoFileName)
            } catch (e: Exception) {
                // ignore since it is failing often because of bugs in Remote driver for Chrome
                e.printStackTrace()
            }
        }
    }

    override fun preAfterTest(testResult: TestResult, memberInfo: Method) {
        if (isEnabled) {
            val videoSaveDir = outputFolder
            val videoFileName = getUniqueFileName(memberInfo.name)
            val videoFullPath: String = Paths.get(videoSaveDir, videoFileName).toString()
            FFMPEG_VIDEO_RECORDER.close()
            if (testResult == TestResult.FAILURE) {
                VIDEO_GENERATED.broadcast(VideoPluginEventArgs(VIDEO_FULL_PATH.get()))
            } else {
                FileUtils.forceDeleteOnExit(File(videoFullPath))
            }
        }
    }

    protected fun takeVideo(screenshotSaveDir: String, filename: String) {
        val videoFullPath = FFmpegVideoRecorder().startRecording(screenshotSaveDir, filename)
        VIDEO_FULL_PATH.set(videoFullPath)
    }
}