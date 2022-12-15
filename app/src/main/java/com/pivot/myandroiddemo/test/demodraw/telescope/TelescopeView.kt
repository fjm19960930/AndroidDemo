package com.pivot.myandroiddemo.test.demodraw.telescope

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.pivot.myandroiddemo.R

/**
 * 望远镜
 */
class TelescopeView(context: Context?, attrs: AttributeSet?): View(context, attrs) {

    constructor(context: Context?): this(context, null)

    private var mPaint: Paint? = null
    private var mBitmap: Bitmap? = null
    private var mBitmapBg: Bitmap? = null
    private var mDx = -1
    private var mDy = -1
    private var mRect = Rect()
    private var mCanvasBg = Canvas()

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)//禁用硬件加速
        mPaint = Paint()
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.scene)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mDx = event.x.toInt()
                mDy = event.y.toInt()
                postInvalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                mDx = event.x.toInt()
                mDy = event.y.toInt()
                postInvalidate()
                return true
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                postInvalidate()
//                mDx = -1
//                mDy = -1
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBitmapBg == null) {
            mBitmapBg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        mCanvasBg.setBitmap(mBitmapBg)//获取mBitmapBg的canvas
        mRect.set(0, 0, width, height)
        mCanvasBg.drawBitmap(mBitmap!!, null, mRect, mPaint)
        if (mDx != -1 && mDy != -1) {
            mPaint?.shader = BitmapShader(mBitmapBg!!, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            canvas?.drawCircle(mDx.toFloat(), mDy.toFloat(), 150F, mPaint!!)
        }
    }
}