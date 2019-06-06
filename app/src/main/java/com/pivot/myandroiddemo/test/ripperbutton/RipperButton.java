package com.pivot.myandroiddemo.test.ripperbutton;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;

/**
 * 点击产生水波动画的按钮
 */
public class RipperButton extends android.support.v7.widget.AppCompatButton{
    private int mX, mY;
    private ObjectAnimator mAnimator;
    private static int DEFAULT_RADIUS = 50;
    private int mCurRadius = 0;
    private RadialGradient mRadialGradient;
    private Paint mPaint;
    
    public RipperButton(Context context) {
        super(context);
        init();
    }

    public RipperButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RipperButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mPaint = new Paint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mX != event.getX() || mY != event.getY()) {
            mX = (int) event.getX();
            mY = (int) event.getY();

            setRadius(DEFAULT_RADIUS);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.cancel();
            }
            if (mAnimator == null) {
                mAnimator = ObjectAnimator.ofInt(this, "radius", DEFAULT_RADIUS, getMeasuredWidth());//半径从初始值扩大到整个控件的宽度,this指定动画作用于本类对象,
                //"radius"指定要操作的控件的属性,注意这个参数值是根据你指定控件的本类中的set方法来查找的属性，而不是根据成员变量名，就比如这里的成员变量名是mCurRadius并不是
                //radius，但它的set方法是setRadius，所以这里的第二个参数就是"radius"，就等于是一直在动态的调用setRadius方法
            }

            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    setRadius(0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            mAnimator.start();
        }
        return super.onTouchEvent(event);
    }

    /**
     * 重置水波半径并重绘
     */
    public void setRadius(final int radius) {
        mCurRadius = radius;
        if (mCurRadius > 0) {
            mRadialGradient = new RadialGradient(mX, mY, mCurRadius, 0x00FFFFFF, 0xFF58FAAC, Shader.TileMode.CLAMP);
            mPaint.setShader(mRadialGradient);
        }
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX, mY, mCurRadius, mPaint);
    }
}
