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

import java.lang.reflect.ParameterizedType

object InstanceFactory {
    inline fun <reified T> create(): T {
        return try {
            T::class.java.constructors[0].newInstance() as T
        } catch (ex: Exception) {
            T::class.objectInstance!!
        }
    }

    inline fun <T> createByClass(tclass: Class<T>): T {
        return tclass.constructors[0].newInstance() as T
    }

    inline fun <reified T> create(vararg args: Any): T {
        return try {
            T::class.java.constructors[0].newInstance(args) as T
        } catch (ex: Exception) {
            T::class.java.constructors[0].newInstance(args) as T
        }
    }

    fun <T> createByTypeParameter(parameterClass: Class<*>, index: Int): T? {
        return try {
            val obj = object : WrapGeneric<T>() {}
            val javaClass = obj.javaClass
            val parameterizedType = javaClass.genericSuperclass as ParameterizedType
            val inputType = parameterizedType.actualTypeArguments[0]
            println(inputType)
            inputType.javaClass.getDeclaredConstructor().newInstance() as T
        } catch (e: java.lang.Exception) {
            null
        }
    }

    open class WrapGeneric<T>
}