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

import io.appium.java_client.MultiTouchAction
import io.appium.java_client.PerformsTouchActions
import io.appium.java_client.TouchAction
import io.appium.java_client.touch.TapOptions
import io.appium.java_client.touch.WaitOptions
import io.appium.java_client.touch.offset.PointOption
import solutions.bellatrix.android.components.AndroidComponent
import solutions.bellatrix.android.infrastructure.DriverService
import java.time.Duration

object TouchActionsService : MobileService() {
    class PlatformTouchAction(performsTouchActions: PerformsTouchActions) : TouchAction<PlatformTouchAction>(performsTouchActions)
    
    private val wrappedMultiAction: MultiTouchAction = MultiTouchAction(DriverService.getWrappedAndroidDriver())
    fun <TComponent : AndroidComponent> tap(component: TComponent, count: Int): TouchActionsService {
        val touchAction: TouchAction<*>  = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.tap(TapOptions.tapOptions().withPosition(PointOption.point(component.location.x, component.location.y)).withTapsCount(count))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun <TComponent : AndroidComponent> press(component: TComponent): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.press(PointOption.point(component.location.x, component.location.y))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun press(x: Int, y: Int): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.press(PointOption.point(x, y))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun <TComponent : AndroidComponent> longPress(component: TComponent): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.longPress(PointOption.point(component.location.x, component.location.y))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun longPress(x: Int, y: Int): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.longPress(PointOption.point(x, y))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun wait(waitTimeMilliseconds: Long): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.waitAction(WaitOptions
                .waitOptions(Duration.ofMillis(waitTimeMilliseconds)))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun <TComponent : AndroidComponent> moveTo(component: TComponent): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.moveTo(PointOption.point(component.location.x, component.location.y))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun moveTo(x: Int, y: Int): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.moveTo(PointOption.point(x, y))
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun release(): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction.release()
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun <TComponent : AndroidComponent> swipe(firstComponent: TComponent, secondComponent: TComponent, duration: Int): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction
                .press(PointOption.point(firstComponent.location.x, firstComponent.location.y))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration.toLong())))
                .moveTo(PointOption.point(secondComponent.location.x, secondComponent.location.y))
                .release().perform()
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun swipe(startx: Int, starty: Int, endx: Int, endy: Int, duration: Int): TouchActionsService {
        val touchAction: TouchAction<*> = PlatformTouchAction(DriverService.getWrappedAndroidDriver())
        touchAction
                .press(PointOption.point(startx, starty))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration.toLong())))
                .moveTo(PointOption.point(endx, endy))
                .release().perform()
        wrappedMultiAction.add(touchAction)
        return this
    }

    fun perform() {
        wrappedMultiAction.perform()
    }
}