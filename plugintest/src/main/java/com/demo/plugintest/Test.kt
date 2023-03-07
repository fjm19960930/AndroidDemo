package com.demo.plugintest

import android.content.Context
import android.util.Log
import android.widget.Toast

object Test {
    fun showSomething(c: Context) {
        Log.e("tag", "我是插件方法")
    }
}