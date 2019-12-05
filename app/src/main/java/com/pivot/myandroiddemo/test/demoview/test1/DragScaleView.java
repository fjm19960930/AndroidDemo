package com.pivot.myandroiddemo.test.demoview.test1;

import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class DragScaleView extends View {
    private static String TAG = "DragScaleView";
    private float RECT_MIN_WIDTH;
    private float RECT_MIN_HEIGHT;

    private Point mActionMovePoint = new Point();
    private ScaleGestureDetector mScaleDetector;//监听图片缩放
    private GestureDetector mGestureDetector;//监听图片移动
    private RectF mRect;
    private RectF mInitRect;//最开始的裁剪框
    private int mPressPointIndex = -1;

    private Paint mPaint = new Paint();
    private Bitmap mSourceBitmap;//原图

    private float mPosX, mPosY; //绘制图片的起始位置
    private float actionDownRectLeft;
    private float actionDownRectTop;
    private float actionDownRectRight;
    private float actionDownRectBottom;

    private float xOffset;
    private float yOffset;

    private Bitmap mNewBitmap;//原图适应控件大小后的新Bitmap对象
    private float mNewWidth;
    private float mNewHeight;
    private float mScaleFactor = 1f;
    private boolean hasGetViewSize;//组件尺寸只需要获取一次
    private UpdateListener mUpdateListener;

    public DragScaleView(Context context) {
        this(context, null);
    }

    public DragScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setImage(Bitmap bitmap) {
        mSourceBitmap = bitmap;
        initViewSize();
        invalidate();
    }
    public void setImageResource(int id) {
        mSourceBitmap = BitmapFactory.decodeResource(getResources(), id);
        initViewSize();
        invalidate();
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.mUpdateListener = updateListener;
    }

    //截取矩形框中的图片
//    public Uri getImageUri() {
//        VivoStorageManager.getInstance().setStorageRootDir(VivoStorageManager.getAvailableSDCardPath());
//        Matrix matrix = new Matrix();
//        matrix.postScale(mScaleFactor, mScaleFactor);
//        Bitmap bitmap = null;
//        float ox = 0, oy = 0;
//        if (mNewBitmap != null && !mNewBitmap.isRecycled()) {
//            bitmap = Bitmap.createBitmap(mNewBitmap, 0, 0, (int) mNewWidth, (int) mNewHeight, matrix, true);
//            ox = (bitmap.getWidth() - mInitRect.width()) / 2 - mPosX + mRect.left;
//            oy = (bitmap.getHeight() - mInitRect.height()) / 2 - mPosY + mRect.top;
//        }
//        if (bitmap!=null && !bitmap.isRecycled()) {
//            Bitmap cutBitmap = Bitmap.createBitmap(bitmap, (int) ox, (int) oy, (int) mRect.width(), (int) mRect.height());
//            return SmartShotUtil.saveImage(cutBitmap, 0, 0, DataCollectConstant.PARAM_FASTSHOT, getContext());
//        } else {
//            return null;
//        }
//    }

    public void setRect(RectF rectF) {
        mRect = new RectF(rectF);
        mInitRect = new RectF(rectF);
    }

    private void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, new SimpleScaleListenerImpl());
        mGestureDetector = new GestureDetector(context, new SimpleGestureListenerImpl());
    }

    private void initViewSize() {
        if (getWidth() > 0 && getHeight() > 0) {
            hasGetViewSize = true;
            float widthScale = 1.0f * (mInitRect.width()) / mSourceBitmap.getWidth();
            float heightScale = 1.0f * (mInitRect.height()) / mSourceBitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(widthScale, heightScale);
            mNewBitmap = Bitmap.createBitmap(mSourceBitmap, 0, 0, mSourceBitmap.getWidth(), mSourceBitmap.getHeight(), matrix, true);
            mNewWidth = mNewBitmap.getWidth();
            mNewHeight = mNewBitmap.getHeight();

            //初始时图片居中绘制
            mPosX = 0;
            mPosY = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mSourceBitmap == null) {
            return;
        }

        if (!hasGetViewSize) {
            initViewSize();
        }
        RECT_MIN_WIDTH = mInitRect.width() / 4 * mScaleFactor;
        RECT_MIN_HEIGHT = mInitRect.height() / 4 * mScaleFactor;
        canvas.save();
        Log.d(TAG, "qqq" + mPosX + ":" + mPosY + "--" + mScaleFactor + "--" + xOffset + ":" + yOffset + "-01");
        checkBounds();
        if (mUpdateListener != null) {
            mUpdateListener.onChange(mPosX, mPosY, mScaleFactor);
        }
        //以图片的中心为基点进行缩放
        canvas.scale(mScaleFactor, mScaleFactor, mPosX + mNewWidth / 2, mPosY + mNewHeight / 2);
        canvas.drawBitmap(mNewBitmap, mPosX, mPosY, null);
        Log.d(TAG, "qqq" + mPosX + ":" + mPosY + "--" + mScaleFactor + "--" + xOffset + ":" + yOffset + "-02");
        canvas.restore();
    }

    float xMaxLeftOffset = 0f;//裁剪框往左方向(包括左上、左、左下)可滑动到的最大偏移量
    float yMaxTopOffset = 0f;//裁剪框往上方向(包括左上、上、右上)可滑动到的最大偏移量
    float xMaxRightOffset = 0f;//裁剪框往右方向(包括右上、右、右下)可滑动到的最大偏移量
    float yMaxBottomOffset = 0f;//裁剪框往下方向(包括左下、下、右下)可滑动到的最大偏移量
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        //双指缩放
        mScaleDetector.onTouchEvent(event);
        //单指移动
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    private float mMaxLeft, mMaxRight, mMaxTop, mMaxBottom;//截图最大可滑动区域在四个方向上的增量
    public void doAnimation(final int index, final float scale, float x, float y, RectF rectF) {
        float xFlow = 0, yFlow = 0;
        float px = mPosX, py = mPosY;
        float dx = x / 2 - rectF.width() * (scale - 1) / 2;
        float dy = y / 2 - rectF.height() * (scale - 1) / 2;
        switch (index) {
            case 0:
                px -= dx;
                py -= dy;
                float xOffset = mPosX - mMaxLeftWithScale;
                float yOffset = mPosY - mMaxTopWithScale;
                xFlow = -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f;
                break;
            case 1:
                px -= dx;
                py -= dy;
                xOffset = Math.max(mPosX - mMaxLeftWithScale, mPosX - mMaxRightWithScale);
                yOffset = mPosY - mMaxTopWithScale;
                xFlow = (xOffset - (mPosX - mMaxLeftWithScale)) < 0.0000001
                        ? -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f
                        : (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f;
                break;
            case 2:
                px += dx;
                py -= dy;
                xOffset = mPosX - mMaxRightWithScale;
                yOffset = mPosY - mMaxTopWithScale;
                xFlow = (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f;
                break;
            case 3:
                px -= dx;
                py -= dy;
                xOffset = mPosX - mMaxLeftWithScale;
                yOffset = Math.max(mPosY - mMaxTopWithScale, mPosY - mMaxBottomWithScale);
                xFlow = -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = (yOffset - (mPosY - mMaxTopWithScale)) < 0.0000001
                        ? -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f
                        : (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f;
                break;
            case 4:
                px += dx;
                py -= dy;
                xOffset = mPosX - mMaxRightWithScale;
                yOffset = Math.max(mPosY - mMaxTopWithScale, mPosY - mMaxBottomWithScale);
                xFlow = (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = (yOffset - (mPosY - mMaxTopWithScale)) < 0.0000001
                        ? -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f
                        : (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f;
                break;
            case 5:
                px -= dx;
                py += dy;
                xOffset = mPosX - mMaxLeftWithScale;
                yOffset = mPosY - mMaxBottomWithScale;
                xFlow = -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f;
                break;
            case 6:
                px -= dx;
                py += dy;
                xOffset = Math.max(mPosX - mMaxLeftWithScale, mPosX - mMaxRightWithScale);
                yOffset = mPosY - mMaxBottomWithScale;
                xFlow = (xOffset - (mPosX - mMaxLeftWithScale)) < 0.0000001
                        ? -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f
                        : (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f;
                break;
            case 7:
                px += dx;
                py += dy;
                xOffset = mPosX - mMaxRightWithScale;
                yOffset = mPosY - mMaxBottomWithScale;
                xFlow = (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f;
                yFlow = (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f;
                break;
        }
        ValueAnimator maxPosAnimator = ValueAnimator.ofObject(new MaxPosEvaluator(), new MaxPosition(mMaxLeft, mMaxRight, mMaxTop, mMaxBottom),
                new MaxPosition(mMaxLeft - dx, mMaxRight + dx, mMaxTop - dy, mMaxBottom + dy));
        ValueAnimator posAnimator = ValueAnimator.ofObject(new PosEvaluator(), new Position(mPosX, mPosY), new Position(px + xFlow, py + yFlow));
//        ValueAnimator rectAnimator = ValueAnimator.ofObject(new RectEvaluator(), mRect, new RectF(left, top, right, bottom));
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(mScaleFactor, mScaleFactor * scale);
        maxPosAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMaxLeft = ((MaxPosition) animation.getAnimatedValue()).getLeft();
                mMaxRight = ((MaxPosition) animation.getAnimatedValue()).getRight();
                mMaxTop = ((MaxPosition) animation.getAnimatedValue()).getTop();
                mMaxBottom = ((MaxPosition) animation.getAnimatedValue()).getBottom();
            }
        });
        posAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPosX = ((Position) animation.getAnimatedValue()).getPx();
                mPosY = ((Position) animation.getAnimatedValue()).getPy();
            }
        });
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScaleFactor = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(maxPosAnimator, posAnimator, scaleAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }


    /**
     * 不能超出边界
     */
    private float mMaxRightWithScale;//算上图片放大后多出来的宽高，图片向右平移所能移到的最远距离
    private float mMaxLeftWithScale;//算上图片放大后多出来的宽高，图片向左平移所能移到的最远距离
    private float mMaxBottomWithScale;//算上图片放大后多出来的宽高，图片向下平移所能移到的最远距离
    private float mMaxTopWithScale;//算上图片放大后多出来的宽高，图片向上平移所能移到的最远距离
    private void checkBounds() {
        if (mScaleFactor >= getWidth() / mNewWidth) {
            //宽度方向已经填满
            mPosX = Math.min(mPosX, (mScaleFactor - 1) * (mNewWidth / 2) + mMaxRight);//最右 mPosX<=
            mPosX = Math.max(mPosX, getWidth() - mNewWidth - (mScaleFactor - 1) * (mNewWidth / 2) + mMaxLeft);//最左 mPosX>=
        } else {
            mPosX = Math.max(mPosX, (mScaleFactor - 1) * (mNewWidth / 2));
            mPosX = Math.min(mPosX, getWidth() - mNewWidth - (mScaleFactor - 1) * (mNewWidth / 2));
        }
        mMaxRightWithScale = (mScaleFactor - 1) * (mNewWidth / 2) + mMaxRight;
        mMaxLeftWithScale = getWidth() - mNewWidth - (mScaleFactor - 1) * (mNewWidth / 2) + mMaxLeft;
        if (mScaleFactor >= getHeight() / mNewHeight) {
            //高度方向已经填满
            mPosY = Math.min(mPosY, (mScaleFactor - 1) * (mNewHeight / 2) + mMaxBottom);//最下 mPosY<=
            mPosY = Math.max(mPosY, getHeight() - mNewHeight - (mScaleFactor - 1) * (mNewHeight / 2) + mMaxTop);//最上 mPosY>=
        } else {
            mPosY = Math.max(mPosY, (mScaleFactor - 1) * (mNewHeight / 2));
            mPosY = Math.min(mPosY, getHeight() - mNewHeight - (mScaleFactor - 1) * (mNewHeight / 2));
        }
        mMaxBottomWithScale = (mScaleFactor - 1) * (mNewHeight / 2) + mMaxBottom;
        mMaxTopWithScale = getHeight() - mNewHeight - (mScaleFactor - 1) * (mNewHeight / 2) + mMaxTop;
    }

    //缩放
    private class SimpleScaleListenerImpl extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 4f));//缩放倍数范围：1～4
            invalidate();
            return true;
        }
    }

    //移动
    private class SimpleGestureListenerImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mPosX -= distanceX;
            mPosY -= distanceY;
            invalidate();
            return true;
        }
    }

    interface UpdateListener {
        void onChange(float posX, float posY, float scale);
    }

    class PosEvaluator implements TypeEvaluator<Position> {
        @Override
        public Position evaluate(float fraction, Position startValue, Position endValue) {
            float x, y;
            x = startValue.getPx() + (endValue.getPx() - startValue.getPx()) * fraction;
            y = startValue.getPy() + (endValue.getPy() - startValue.getPy()) * fraction;
            return new Position(x, y);
        }
    }

    class MaxPosEvaluator implements TypeEvaluator<MaxPosition> {
        @Override
        public MaxPosition evaluate(float fraction, MaxPosition startValue, MaxPosition endValue) {
            float left, right, top, bottom;
            left = startValue.getLeft() + (endValue.getLeft() - startValue.getLeft()) * fraction;
            right = startValue.getRight() + (endValue.getRight() - startValue.getRight()) * fraction;
            top = startValue.getTop() + (endValue.getTop() - startValue.getTop()) * fraction;
            bottom = startValue.getBottom() + (endValue.getBottom() - startValue.getBottom()) * fraction;
            return new MaxPosition(left, right, top, bottom);
        }
    }

    private class MaxPosition {
        private float mLeft;
        private float mRight;
        private float mTop;
        private float mBottom;

        MaxPosition(float left, float right, float top, float bottom) {
            this.mLeft = left;
            this.mRight = right;
            this.mTop = top;
            this.mBottom = bottom;
        }

        public float getLeft() {
            return mLeft;
        }

        public float getRight() {
            return mRight;
        }

        public float getTop() {
            return mTop;
        }

        public float getBottom() {
            return mBottom;
        }
    }

    private class Position {
        private float mPx;
        private float mPy;

        Position(float px, float py) {
            this.mPx = px;
            this.mPy = py;
        }

        public float getPx() {
            return mPx;
        }

        public float getPy() {
            return mPy;
        }
    }
}
