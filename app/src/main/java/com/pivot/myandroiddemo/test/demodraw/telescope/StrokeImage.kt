package com.pivot.myandroiddemo.test.demodraw.telescope

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

/**
 * 单击描边效果
 */
class StrokeImage(context: Context, attributeSet: AttributeSet?): AppCompatImageView(context, attributeSet) {

    constructor(context: Context): this(context, null)

    override fun onFinishInflate() {
        super.onFinishInflate()
        val p = Paint()
        p.color = Color.CYAN
        setStateDrawable(this, p)
    }

    private fun setStateDrawable(image: ImageView, p: Paint) {
        //获取源图
        val bitmapDrawable = image.drawable as BitmapDrawable
        val srcBitmap = bitmapDrawable.bitmap
        //绘制纯色背景
        val bitmap = Bitmap.createBitmap(srcBitmap.width, srcBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(srcBitmap.extractAlpha(), 0f, 0f, p)
        //添加按压响应 等同于：
        //<selector xmlns:android= ” http://schemas.android.com/apk/res/android">
        //<item android:state pressed="true" android:state checked="true" drawable="@drawable/xxx"/>
        //</selector>
        val sld = StateListDrawable()
        sld.addState(intArrayOf(android.R.attr.state_pressed), BitmapDrawable(bitmap))

        image.setBackgroundDrawable(sld)
    }
}