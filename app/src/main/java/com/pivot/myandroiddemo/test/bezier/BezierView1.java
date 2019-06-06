package com.pivot.myandroiddemo.test.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ASUS on 2019/5/31.
 */
public class BezierView1 extends View {
    private Path mPath = new Path();
    private float mPreX;
    private float mPreY;

    public BezierView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPath.moveTo(event.getX(), event.getY());//将Path的初始位置设置到手指的触点处，如果不调用mPath.moveTo的话，会默认是从(0,0)开始的
                mPreX = event.getX();
                mPreY = event.getY();
                return true;//return true让ACTION_MOVE、ACTION_UP事件继续向这个控件传递
            }
            case MotionEvent.ACTION_MOVE:
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = event.getX();
                mPreY = event.getY();
                postInvalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, paint);
    }

    public void reset() {
        mPath.reset();
        invalidate();
    }
}
