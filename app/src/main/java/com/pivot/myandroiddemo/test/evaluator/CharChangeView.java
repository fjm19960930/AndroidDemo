package com.pivot.myandroiddemo.test.evaluator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * 字母轮播
 */
public class CharChangeView extends View {
    private char letter = 'A';
    private Paint paint = new Paint();
    private ValueAnimator animator;
    private CharChangeListener listener;

    public CharChangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();// 获取对应高度    
        int width = getWidth(); // 获取对应宽度
        paint.setColor(Color.parseColor("#3385ff"));
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置字体
        paint.setAntiAlias(true);//设为抗锯齿
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int baseline = height / 2 + (fm.bottom - fm.top) / 2 - fm.bottom;
        canvas.drawText(String.valueOf(letter), width / 2, baseline, paint);
        paint.reset();// 重置画笔   
    }

    public void startAnimation() {
        animator = ValueAnimator.ofObject(new CharEvaluator(), new Character('A'), new Character('Z'));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                letter = (char) animation.getAnimatedValue();
                if (listener != null) {
                    listener.currentStatus(String.valueOf(letter));
                }
                invalidate();//必须在UI线程中被调用
//                postInvalidate();//可以在任意线程中调用
            }
        });
        animator.setDuration(10000);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    public ValueAnimator getAnimator() {
        return animator;
    }

    public void setListener(CharChangeListener listener) {
        this.listener = listener;
    }

    public interface CharChangeListener {
        void currentStatus(String now);
        void pauseStatus(String pause);
    }
}
