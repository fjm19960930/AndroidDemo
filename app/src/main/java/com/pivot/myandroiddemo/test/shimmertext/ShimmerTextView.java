package com.pivot.myandroiddemo.test.shimmertext;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.pivot.myandroiddemo.R;

/**
 * 发光文本
 * 参考自：https://blog.csdn.net/harvic880925/article/details/52350154
 */
public class ShimmerTextView extends android.support.v7.widget.AppCompatTextView {
    private Paint mPaint;
    private int mDx;
    private LinearGradient mLinearGradient;
    
    int scrollBackgroundColor;//滚动得文本背景色
    boolean isRepeat;//是否重复滚动
    public ShimmerTextView(Context context) {
        super(context);
        init(null);
    }

    public ShimmerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShimmerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mPaint = getPaint();
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ShimmerTextView);
        scrollBackgroundColor = array.getColor(R.styleable.ShimmerTextView_scrollBackgroundColor, 0xffff00ff);
        isRepeat = array.getBoolean(R.styleable.ShimmerTextView_isRepeat, true);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ValueAnimator animator = ValueAnimator.ofInt(0, 2 * getMeasuredWidth());//动画运行的长度范围是从0到当前控件宽度的两倍
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDx = (Integer) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(isRepeat ? ValueAnimator.INFINITE : 1);
        animator.setDuration(2000);
        animator.start();

        mLinearGradient = new LinearGradient(-getMeasuredWidth(), 0, 0, 0, new int[]{
                getCurrentTextColor(), scrollBackgroundColor, getCurrentTextColor()
        }, new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(mDx, 0);//初始位置是-getMeasuredWidth()，动画的终止位置是-getMeasuredWidth()+2*getMeasuredWidth()，即getMeasuredWidth()
        mLinearGradient.setLocalMatrix(matrix);
        mPaint.setShader(mLinearGradient);

        super.onDraw(canvas);
    }
}
