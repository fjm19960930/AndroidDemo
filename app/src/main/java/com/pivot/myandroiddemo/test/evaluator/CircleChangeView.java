package com.pivot.myandroiddemo.test.evaluator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by ASUS on 2019/6/1.
 */

public class CircleChangeView extends View {

    private Paint paint;
    private Circle circle;

    public CircleChangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (circle != null) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#00ff00"));
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, circle.getRadius(), paint);
        }
    }
    
    public void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofObject(new CircleEvaluator(), new Circle(10), new Circle(200));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circle = (Circle) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(3000);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }
}
