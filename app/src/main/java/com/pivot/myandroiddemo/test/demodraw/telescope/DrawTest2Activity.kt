package com.pivot.myandroiddemo.test.demodraw.telescope

import android.os.Bundle
import com.pivot.myandroiddemo.R
import com.pivot.myandroiddemo.base.ActivityParam
import com.pivot.myandroiddemo.base.BaseActivity

@ActivityParam(isShowToolBar = false)
class DrawTest2Activity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_test2)
    }
}