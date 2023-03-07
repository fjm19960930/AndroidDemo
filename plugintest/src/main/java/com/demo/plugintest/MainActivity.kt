package com.demo.plugintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dalvik.system.DexClassLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dexPath = cacheDir.absolutePath

        val cl = DexClassLoader(dexPath, cacheDir.absolutePath, null, classLoader)//自定义的类加载器
        val clazz = cl.loadClass("com.demo.plugintest.MainActivity")//加载MainActivity获取其class对象
    }
}