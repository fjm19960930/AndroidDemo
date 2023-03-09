package com.pivot.myandroiddemo.util

import android.content.Context
import dalvik.system.DexClassLoader
import java.lang.reflect.Array
import java.lang.reflect.Field

object PluginUtils {

    //合并dexElements
    fun loadClass(context: Context) {
        val pathClassLoaderClazz: Class<*> = Class.forName("dalvik.system.BaseDexClassLoader")
        val pathListField: Field = pathClassLoaderClazz.getDeclaredField("pathList")//pathList的Field对象
        pathListField.isAccessible = true
        val dexPathListClazz: Class<*> = Class.forName("dalvik.system.DexPathList")
        val dexElementsField: Field = dexPathListClazz.getDeclaredField("dexElements")//dexElements的Field对象
        dexElementsField.isAccessible = true

        val hostClassLoader = context.classLoader//宿主的类加载器对象
        val hostPathList: Any = pathListField.get(hostClassLoader)//宿主的DexPathList对象
        val hostDexElements = dexElementsField.get(hostPathList) as kotlin.Array<*>//宿主的dexElements对象

        val pluginClassLoader = DexClassLoader(context.cacheDir.path+"/plugintest-debug.apk",
            context.cacheDir.path, null, hostClassLoader)//插件的类加载器对象
        val pluginPathList: Any = pathListField.get(pluginClassLoader)//插件的DexPathList对象
        val pluginDexElements = dexElementsField.get(pluginPathList) as kotlin.Array<*>//插件的dexElements对象

        val newDexElements: kotlin.Array<*> = Array.newInstance(
            hostDexElements.javaClass.componentType,
            hostDexElements.size + pluginDexElements.size
        ) as kotlin.Array<*>//通过反射创建一个新的Element数组
        System.arraycopy(hostDexElements, 0, newDexElements, 0, hostDexElements.size)//将宿主的dexElements复制给newDexElements
        System.arraycopy(pluginDexElements, 0, newDexElements, hostDexElements.size, pluginDexElements.size)//将插件的dexElements复制给newDexElements

        dexElementsField.set(hostPathList, newDexElements)//将合并后的dexElements赋值给宿主的dexElements
    }

}