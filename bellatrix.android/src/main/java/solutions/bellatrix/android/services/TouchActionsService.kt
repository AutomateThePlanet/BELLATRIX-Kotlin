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

object TouchActionsService : MobileService() {
//    private val wrappedMultiAction: MultiTouchAction = MultiTouchAction(DriverService.getWrappedAndroidDriver())
//    fun <TComponent : AndroidComponent?> tap(component: TComponent, count: Int): TouchActionsService {
//        val touchAction: TouchAction = TouchAction(DriverService.getWrappedAndroidDriver())
//        touchAction.tap(TapOptions.tapOptions()
//                .withPosition(PointOption.point(component.getLocation().getX(), component.getLocation().getY())).withTapsCount(count))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun <TComponent : AndroidComponent?> press(component: TComponent): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.press(PointOption.point(component.getLocation().getX(), component.getLocation().getY()))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun press(x: Int?, y: Int?): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.press(PointOption.point(x, y))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun <TComponent : AndroidComponent?> longPress(component: TComponent): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.longPress(PointOption.point(component.getLocation().getX(), component.getLocation().getY()))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun longPress(x: Int?, y: Int?): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.longPress(PointOption.point(x, y))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun wait(waitTimeMilliseconds: Long?): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.waitAction(WaitOptions
//                .waitOptions(Duration.ofMillis(waitTimeMilliseconds!!)))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun <TComponent : AndroidComponent?> moveTo(component: TComponent): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.moveTo(PointOption.point(component.getLocation().getX(), component.getLocation().getY()))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun moveTo(x: Int?, y: Int?): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.moveTo(PointOption.point(x, y))
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun release(): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction.release()
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun <TComponent : AndroidComponent?> swipe(firstComponent: TComponent, secondComponent: TComponent, duration: Int): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction
//                .press(PointOption.point(firstComponent.getLocation().getX(), firstComponent.getLocation().getY()))
//                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration.toLong())))
//                .moveTo(PointOption.point(secondComponent.getLocation().getX(), secondComponent.getLocation().getY()))
//                .release().perform()
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun swipe(startx: Int, starty: Int, endx: Int, endy: Int, duration: Int): TouchActionsService {
//        val touchAction: TouchAction<*> = TouchAction<Any?>(DriverService.getWrappedAndroidDriver())
//        touchAction
//                .press(PointOption.point(startx, starty))
//                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration.toLong())))
//                .moveTo(PointOption.point(endx, endy))
//                .release().perform()
//        wrappedMultiAction.add(touchAction)
//        return this
//    }
//
//    fun perform() {
//        wrappedMultiAction.perform()
//    }
}