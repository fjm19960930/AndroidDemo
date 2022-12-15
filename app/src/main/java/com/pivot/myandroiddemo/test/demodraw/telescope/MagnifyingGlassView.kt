package com.pivot.myandroiddemo.test.demodraw.telescope

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.pivot.myandroiddemo.R

/**
 * 放大镜
 */
class MagnifyingGlassView(context: Context?, attributeSet: AttributeSet?): View(context, attributeSet) {

    constructor(context: Context?): this(context, null)

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private var mBitmap: Bitmap? = null
    private var mShapeDrawable: ShapeDrawable? = null
    private val mMatrix: Matrix = Matrix()
    companion object {
        private const val RADIUS = 80//放大镜半径
        private const val FACTOR = 3//放大倍数
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x?.toInt()
        val y = event?.y?.toInt()
        mMatrix.setTranslate((RADIUS - FACTOR * x!!).toFloat(), (RADIUS - FACTOR * y!!).toFloat())
        mShapeDrawable?.paint?.shader?.setLocalMatrix(mMatrix)
        mShapeDrawable?.setBounds(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBitmap == null) {
            val bmp = BitmapFactory.decodeResource(resources, R.drawable.scene)
            mBitmap = Bitmap.createScaledBitmap(bmp, width, height, false)
            val shader = BitmapShader(Bitmap.createScaledBitmap(mBitmap!!, FACTOR * mBitmap?.width!!,
                FACTOR * mBitmap?.height!!, true), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)//放大FACTOR倍
            mShapeDrawable = ShapeDrawable(OvalShape())
            mShapeDrawable?.paint?.shader = shader
            mShapeDrawable?.setBounds(0, 0, RADIUS * 2, RADIUS * 2)//设置形成椭圆的矩形宽高，都是两倍的半径即直径
        }
        canvas?.drawBitmap(mBitmap!!, 0F, 0F, null)
        mShapeDrawable?.draw(canvas!!)
    }
}