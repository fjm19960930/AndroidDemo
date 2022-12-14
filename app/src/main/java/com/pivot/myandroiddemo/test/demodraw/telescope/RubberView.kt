package com.pivot.myandroiddemo.test.demodraw.telescope

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.pivot.myandroiddemo.R

/**
 * 橡皮擦/刮刮卡
 */
class RubberView(context: Context?, attrs: AttributeSet?): View(context, attrs) {

    constructor(context: Context?): this(context, null)

    private var mPath: Path? = null
    private var mPaint: Paint? = null
    private var mBitmapDST: Bitmap? = null
    private var mBitmapSRC: Bitmap? = null
    private var mBitmapTEXT: Bitmap? = null
    private var mPreX = 0f
    private var mPreY = 0f
    private var mRect = RectF()
    private var mRectSrc = Rect()
    private var pdxf = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    private var mCanvasDST = Canvas()

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)//禁用硬件加速
        mPath = Path()
        mPaint = Paint()
        mPaint?.color = Color.RED
        mPaint?.style = Paint.Style.STROKE
        mPaint?.strokeWidth = 45F
        val option = BitmapFactory.Options()
        option.inSampleSize = 2
        mBitmapSRC = BitmapFactory.decodeResource(resources, R.drawable.scene, option)
        mBitmapDST = Bitmap.createBitmap(mBitmapSRC?.width!!, mBitmapSRC?.height!!, Bitmap.Config.ARGB_8888)
        mBitmapTEXT = BitmapFactory.decodeResource(resources, R.drawable.gxn, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath?.moveTo(
                    event.x,
                    event.y
                ) //将Path的初始位置设置到手指的触点处，如果不调用mPath.moveTo的话，会默认是从(0,0)开始的
                mPreX = event.x
                mPreY = event.y
                return true //return true让ACTION_MOVE、ACTION_UP事件继续向这个控件传递
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = (mPreX + event.x) / 2
                val endY = (mPreY + event.y) / 2
                mPath?.quadTo(mPreX, mPreY, endX, endY)
                mPreX = event.x
                mPreY = event.y
            }
        }
        postInvalidate()
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mRect.set(0f, 0f, mBitmapDST?.width?.toFloat()!!, mBitmapDST?.height?.toFloat()!!)
        mRectSrc.set(0, 0, mBitmapDST?.width!!, mBitmapDST?.height!!)
        //绘制顶层结果图片
        canvas?.drawBitmap(mBitmapTEXT!!, mRectSrc, mRect, mPaint)
        //save图层
        val layerId = canvas?.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        //将手势轨迹绘制到目标图片上
        mCanvasDST.setBitmap(mBitmapDST)
        mCanvasDST.drawPath(mPath!!, mPaint!!)
        canvas?.drawBitmap(mBitmapDST!!, 0f, 0f, mPaint)
        //计算源图像区域
        mPaint?.xfermode = pdxf
        canvas?.drawBitmap(mBitmapSRC!!, 0f, 0f, mPaint)

        mPaint?.xfermode = null
        canvas?.restoreToCount(layerId!!)
    }

}