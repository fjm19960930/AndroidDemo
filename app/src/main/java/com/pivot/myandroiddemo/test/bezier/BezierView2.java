package com.pivot.myandroiddemo.test.bezier;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by ASUS on 2019/5/31.
 */
public class BezierView2 extends View {
    private Paint mPaint;
    private Path mPath;
    private int dx;
    private int mItemWaveLength = 400;//一个波的长度

    public BezierView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        int originY = 300;
        int halfWaveLen = mItemWaveLength / 2;
        mPath.moveTo(-mItemWaveLength + dx, originY);
        for (int i = -mItemWaveLength; i <= getWidth() + mItemWaveLength; i += mItemWaveLength) {
            mPath.rQuadTo(halfWaveLen / 2, -100, halfWaveLen, 0);
            mPath.rQuadTo(halfWaveLen / 2, 100, halfWaveLen, 0);
        }
        mPath.lineTo(getWidth(), getHeight());
        mPath.lineTo(0, getHeight());
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    public void startAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mItemWaveLength);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);//动画无限循环

        //设置运动插值器，决定是线性运动还是非线性运动
        //LinearInterpolator 匀速
        //AccelerateDecelerateInterpolator 先加速再减速
        //AccelerateInterpolator 持续加速，瞬间停止
        //DecelerateInterpolator 持续减速，动画结束速度减为0
        //AnticipateInterpolator 先回拉再进行正常的动画轨迹，比如动画是要控件匀速变大，使用这个插值器会使控件先变小再正常匀速变大，类似于一个蓄力的感觉
        //OvershootInterpolator 它和上面一个插值器正好相反，比如控件匀速变大到指定大小，使用这个插值器就会使控件在变大到指定大小后继续变大一点，然后再弹回指定大小
        //AnticipateOvershootInterpolator 它是上面两个插值器的结合体，不多说了
        //BounceInterpolator 在目标值出弹跳，类似于乒乓球落地后来回起落的效果
        //CycleInterpolator 它是一个正余弦曲线，但是他和AnticipateOvershootInterpolator的区别是：它可以自定义曲线的周期，所以动画可以不到终点就结束，也可以到达终点后回弹，回弹的次数由曲线的周期决定，曲线的周期由 CycleInterpolator() 构造方法的参数决定
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dx = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}
