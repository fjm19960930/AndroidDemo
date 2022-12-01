package com.pivot.myandroiddemo.test.demoanimation.test4

import android.graphics.drawable.Animatable
import com.pivot.myandroiddemo.base.ActivityParam
import com.pivot.myandroiddemo.base.BaseActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.pivot.myandroiddemo.R

@ActivityParam(isShowToolBar = false)
class AnimationTest4Activity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation_test4)
        val iv = getView<ImageView>(R.id.iv)
        val avdc = AnimatedVectorDrawableCompat.create(this, R.drawable.line_animated_vector)
        iv.setImageDrawable(avdc)
        (iv.drawable as Animatable).start()

        val inputIv = getView<ImageView>(R.id.iv_input)
        val inputEt = getView<EditText>(R.id.et)
        inputIv.isFocusable = true
        inputIv.isFocusableInTouchMode = true
        inputIv.requestFocus()
        inputIv.requestFocusFromTouch()
        inputEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                inputEt.hint = ""
                val avdc1 = AnimatedVectorDrawableCompat.create(this, R.drawable.input_animated_vector)
                inputIv.setImageDrawable(avdc1)
                (inputIv.drawable as Animatable).start()
            }
        }
    }
}