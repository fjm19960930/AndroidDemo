package com.pivot.myandroiddemo.scrollbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.pivot.myandroiddemo.R;

/**
 * create by fanjiaming 2020/12/16
 */
public class ScrollBar extends View {
    static final int SIDE_OFFSET = 30;
    Rect mHandleHold;
    Paint mPaint = new Paint();
    Context mContext;
    Bitmap mBitmap;
    Rect newRect;

    public ScrollBar(Context context) {
        this(context, null);
    }

    public ScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mHandleHold = new Rect();
        newRect = new Rect();
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.drag_handle);
    }

    @Override
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mHandleHold.set(getLeft(), getTop(), getRight(), getBottom());
        newRect.set(getLeft(), getTop() - SIDE_OFFSET, getRight(), getBottom() - SIDE_OFFSET);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mHandleHold, newRect, mPaint);
    }
}
