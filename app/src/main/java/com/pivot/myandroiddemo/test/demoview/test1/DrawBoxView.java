package com.pivot.myandroiddemo.test.demoview.test1;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("AppCompatCustomView")
public class DrawBoxView extends View {
    private Point mActionMovePoint = new Point();
    private RectF mRect;
    private RectF mInitRect;//最开始的裁剪框
    private RectF mRectLeft;//左侧矩形阴影区域
    private RectF mRectTop;//上侧矩形阴影区域
    private RectF mRectRight;//右侧矩形阴影区域
    private RectF mRectBottom;//下侧矩形阴影区域
    private int mPressPointIndex = -1;
    private float actionDownRectLeft;
    private float actionDownRectTop;
    private float actionDownRectRight;
    private float actionDownRectBottom;
    private CropListener mCropListener;
    private float xOffset;
    private float yOffset;
    private float mScaleFactor = 1f;
    private float mPosX, mPosY; //绘制图片的起始位置
    private float RECT_MIN_WIDTH;
    private float RECT_MIN_HEIGHT;
    private int rectOffset;

    public DrawBoxView(Context context) {
        this(context, null);
    }

    public DrawBoxView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mRectLeft = new RectF();
        mRectTop = new RectF();
        mRectRight = new RectF();
        mRectBottom = new RectF();
    }

    public void setRect(RectF rectF) {
        mRect = new RectF(rectF);
        mInitRect = new RectF(rectF);
    }

    public void setRectOffset(int rectOffset) {
        this.rectOffset = rectOffset;
    }

    public void setCropListener(CropListener mCropListener) {
        this.mCropListener = mCropListener;
    }

    public void setValues(float posX, float posY, float scaleFactor) {
        this.mScaleFactor = scaleFactor;
        this.mPosX = posX;
        this.mPosY = posY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RECT_MIN_WIDTH = mInitRect.width() / 4 * mScaleFactor;
        RECT_MIN_HEIGHT = mInitRect.height() / 4 * mScaleFactor;
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#88000000"));
        paint.setStyle(Paint.Style.FILL);
        mRectLeft.set(rectOffset, rectOffset, getWidth()-rectOffset, mRect.top);
        mRectTop.set(rectOffset, mRect.top, mRect.left, mRect.bottom);
        mRectRight.set(mRect.right, mRect.top, getWidth()-rectOffset, mRect.bottom);
        mRectBottom.set(rectOffset, mRect.bottom, getWidth()-rectOffset, getHeight()-rectOffset);
        canvas.drawRect(mRectLeft, paint);
        canvas.drawRect(mRectTop, paint);
        canvas.drawRect(mRectRight, paint);
        canvas.drawRect(mRectBottom, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);//设置填充样式
        paint.setStrokeWidth(4);
        canvas.drawRect(mRect, paint);

        float midHorizontal = (mRect.left + mRect.right) / 2;//水平方向中点
        float midVertical = (mRect.top + mRect.bottom) / 2;//垂直方向中点
        paint.setStrokeWidth(10);
        float[] pts = {mRect.left - 5, mRect.top, mRect.left + 40, mRect.top,
                midHorizontal - 20, mRect.top, midHorizontal + 20, mRect.top,
                mRect.right - 40, mRect.top, mRect.right + 5, mRect.top,

                mRect.left, mRect.top, mRect.left, mRect.top + 40,
                mRect.right, mRect.top, mRect.right, mRect.top + 40,
                mRect.left, midVertical - 20, mRect.left, midVertical + 20,
                mRect.right, midVertical - 20, mRect.right, midVertical + 20,
                mRect.left, mRect.bottom, mRect.left, mRect.bottom - 40,
                mRect.right, mRect.bottom, mRect.right, mRect.bottom - 40,

                mRect.left - 5, mRect.bottom, mRect.left + 40, mRect.bottom,
                midHorizontal - 20, mRect.bottom, midHorizontal + 20, mRect.bottom,
                mRect.right - 40, mRect.bottom, mRect.right + 5, mRect.bottom};
        canvas.drawLines(pts, paint);
    }

    float xMaxLeftOffset = 0f;//裁剪框往左方向(包括左上、左、左下)可滑动到的最大偏移量
    float yMaxTopOffset = 0f;//裁剪框往上方向(包括左上、上、右上)可滑动到的最大偏移量
    float xMaxRightOffset = 0f;//裁剪框往右方向(包括右上、右、右下)可滑动到的最大偏移量
    float yMaxBottomOffset = 0f;//裁剪框往下方向(包括左下、下、右下)可滑动到的最大偏移量
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mActionMovePoint.x = (int) event.getX();
        mActionMovePoint.y = (int) event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                actionDownRectLeft = mRect.left;
                actionDownRectTop = mRect.top;
                actionDownRectRight = mRect.right;
                actionDownRectBottom = mRect.bottom;
                xMaxLeftOffset = mRect.width() == mInitRect.width() ? 0 : (mPosX - (mScaleFactor - 1) * (mInitRect.width() / 2));
                yMaxTopOffset = mRect.height() == mInitRect.height() ? 0 : (mPosY - (mScaleFactor - 1) * (mInitRect.height() / 2));
                xMaxRightOffset = mRect.width() == mInitRect.width() ? 0 : (mPosX + (mScaleFactor - 1) * (mInitRect.width() / 2));
                yMaxBottomOffset = mRect.height() == mInitRect.height() ? 0 : (mPosY + (mScaleFactor - 1) * (mInitRect.height() / 2));
                return getPosition(mActionMovePoint.x, mActionMovePoint.y) != -1;//如果焦点不在八个点上则不消耗事件，由DragScaleView来处理
            case MotionEvent.ACTION_MOVE:
                switch (getPosition(mActionMovePoint.x, mActionMovePoint.y)) {
                    case 0:
                        mRect.left = mActionMovePoint.x;
                        mRect.top = mActionMovePoint.y;
                        xOffset = mActionMovePoint.x - actionDownRectLeft;
                        yOffset = mActionMovePoint.y - actionDownRectTop;
                        //设置裁剪框可缩放到的最小区域大小以及裁剪之后x、y方向的偏移值
                        if ((actionDownRectRight - mActionMovePoint.x) < RECT_MIN_WIDTH) {
                            mRect.left = actionDownRectRight - RECT_MIN_WIDTH;
                            xOffset = mRect.left - actionDownRectLeft;
                        }
                        if ((actionDownRectBottom - mActionMovePoint.y) < RECT_MIN_HEIGHT) {
                            mRect.top = actionDownRectBottom - RECT_MIN_HEIGHT;
                            yOffset = mRect.top - actionDownRectTop;
                        }
                        //设置裁剪框可缩放到的最大区域大小以及裁剪之后x、y方向的偏移值
                        float xMaxLeft = mInitRect.left + xMaxLeftOffset < 0 ? 0 : mInitRect.left + xMaxLeftOffset;
                        float yMaxTop = mInitRect.top + yMaxTopOffset < 0 ? 0 : mInitRect.top + yMaxTopOffset;
                        if (mActionMovePoint.x < xMaxLeft) {
                            mRect.left = xMaxLeft;
                            xOffset = mRect.left - actionDownRectLeft;
                        }
                        if (mActionMovePoint.y < yMaxTop) {
                            mRect.top = yMaxTop;
                            yOffset = mRect.top - actionDownRectTop;
                        }
                        mPressPointIndex = 0;
                        break;
                    case 1:
                        mRect.top = mActionMovePoint.y;
                        xOffset = 0;
                        yOffset = mActionMovePoint.y - actionDownRectTop;
                        if ((actionDownRectBottom - mActionMovePoint.y) < RECT_MIN_HEIGHT) {
                            mRect.top = actionDownRectBottom - RECT_MIN_HEIGHT;
                            yOffset = mRect.top - actionDownRectTop;
                        }
                        yMaxTop = mInitRect.top + yMaxTopOffset < 0 ? 0 : mInitRect.top + yMaxTopOffset;
                        if (mActionMovePoint.y < yMaxTop) {
                            mRect.top = yMaxTop;
                            yOffset = mRect.top - actionDownRectTop;
                        }
                        mPressPointIndex = 1;
                        break;
                    case 2:
                        mRect.right = mActionMovePoint.x;
                        mRect.top = mActionMovePoint.y;
                        xOffset = actionDownRectRight - mActionMovePoint.x;
                        yOffset = mActionMovePoint.y - actionDownRectTop;
                        if ((mActionMovePoint.x - actionDownRectLeft) < RECT_MIN_WIDTH) {
                            mRect.right = actionDownRectLeft + RECT_MIN_WIDTH;
                            xOffset = actionDownRectRight -  mRect.right;
                        }
                        if ((actionDownRectBottom - mActionMovePoint.y) < RECT_MIN_HEIGHT) {
                            mRect.top = actionDownRectBottom - RECT_MIN_HEIGHT;
                            yOffset = mRect.top - actionDownRectTop;
                        }
                        float xMaxRight = mInitRect.right + xMaxRightOffset > mInitRect.right ? mInitRect.right : mInitRect.right + xMaxRightOffset;
                        yMaxTop = mInitRect.top + yMaxTopOffset < 0 ? 0 : mInitRect.top + yMaxTopOffset;
                        if (mActionMovePoint.x > xMaxRight) {
                            mRect.right = xMaxRight;
                            xOffset = actionDownRectRight - mRect.right;
                        }
                        if (mActionMovePoint.y < yMaxTop) {
                            mRect.top = yMaxTop;
                            yOffset = mRect.top - actionDownRectTop;
                        }
                        mPressPointIndex = 2;
                        break;
                    case 3:
                        mRect.left = mActionMovePoint.x;
                        xOffset = mActionMovePoint.x - actionDownRectLeft;
                        yOffset = 0;
                        if ((actionDownRectRight - mActionMovePoint.x) < RECT_MIN_WIDTH) {
                            mRect.left = actionDownRectRight - RECT_MIN_WIDTH;
                            xOffset = mRect.left - actionDownRectLeft;
                        }
                        xMaxLeft = mInitRect.left + xMaxLeftOffset < 0 ? 0 : mInitRect.left + xMaxLeftOffset;
                        if (mActionMovePoint.x < xMaxLeft) {
                            mRect.left = xMaxLeft;
                            xOffset = mRect.left - actionDownRectLeft;
                        }
                        mPressPointIndex = 3;
                        break;
                    case 4:
                        mRect.right = mActionMovePoint.x;
                        xOffset = actionDownRectRight - mActionMovePoint.x;
                        yOffset = 0;
                        if ((mActionMovePoint.x - actionDownRectLeft) < RECT_MIN_WIDTH) {
                            mRect.right = actionDownRectLeft + RECT_MIN_WIDTH;
                            xOffset = actionDownRectRight - mRect.right;
                        }
                        xMaxRight = mInitRect.right + xMaxRightOffset > mInitRect.right ? mInitRect.right : mInitRect.right + xMaxRightOffset;
                        if (mActionMovePoint.x > xMaxRight) {
                            mRect.right = xMaxRight;
                            xOffset = actionDownRectRight - mRect.right;
                        }
                        mPressPointIndex = 4;
                        break;
                    case 5:
                        mRect.left = mActionMovePoint.x;
                        mRect.bottom = mActionMovePoint.y;
                        xOffset = mActionMovePoint.x - actionDownRectLeft;
                        yOffset = actionDownRectBottom - mActionMovePoint.y;
                        if ((actionDownRectRight - mActionMovePoint.x) < RECT_MIN_WIDTH) {
                            mRect.left = actionDownRectRight - RECT_MIN_WIDTH;
                            xOffset = mRect.left - actionDownRectLeft;
                        }
                        if ((mActionMovePoint.y - actionDownRectTop) < RECT_MIN_HEIGHT) {
                            mRect.bottom = actionDownRectTop + RECT_MIN_HEIGHT;
                            yOffset = actionDownRectBottom - mRect.bottom;
                        }
                        xMaxLeft = mInitRect.left + xMaxLeftOffset < 0 ? 0 : mInitRect.left + xMaxLeftOffset;
                        float yMaxBottom = mInitRect.bottom + yMaxBottomOffset >mInitRect.bottom ? mInitRect.bottom : mInitRect.bottom + yMaxBottomOffset;
                        if (mActionMovePoint.x < xMaxLeft) {
                            mRect.left = xMaxLeft;
                            xOffset = mRect.left - actionDownRectLeft;
                        }
                        if (mActionMovePoint.y > yMaxBottom) {
                            mRect.bottom = yMaxBottom;
                            yOffset = actionDownRectBottom - mRect.bottom;
                        }
                        mPressPointIndex = 5;
                        break;
                    case 6:
                        mRect.bottom = mActionMovePoint.y;
                        xOffset = 0;
                        yOffset = actionDownRectBottom - mActionMovePoint.y;
                        if ((mActionMovePoint.y - actionDownRectTop) < RECT_MIN_HEIGHT) {
                            mRect.bottom = actionDownRectTop + RECT_MIN_HEIGHT;
                            yOffset = actionDownRectBottom - mRect.bottom;
                        }
                        yMaxBottom = mInitRect.bottom + yMaxBottomOffset >mInitRect.bottom ? mInitRect.bottom : mInitRect.bottom + yMaxBottomOffset;
                        if (mActionMovePoint.y > yMaxBottom) {
                            mRect.bottom = yMaxBottom;
                            yOffset = actionDownRectBottom - mRect.bottom;
                        }
                        mPressPointIndex = 6;
                        break;
                    case 7:
                        mRect.right = mActionMovePoint.x;
                        mRect.bottom = mActionMovePoint.y;
                        xOffset = actionDownRectRight - mActionMovePoint.x;
                        yOffset = actionDownRectBottom - mActionMovePoint.y;
                        if ((mActionMovePoint.x - actionDownRectLeft) < RECT_MIN_WIDTH) {
                            mRect.right = actionDownRectLeft + RECT_MIN_WIDTH;
                            xOffset = actionDownRectRight - mRect.right;
                        }
                        if ((mActionMovePoint.y - actionDownRectTop) < RECT_MIN_HEIGHT) {
                            mRect.bottom = actionDownRectTop + RECT_MIN_HEIGHT;
                            yOffset = actionDownRectBottom - mRect.bottom;
                        }
                        xMaxRight = mInitRect.right + xMaxRightOffset > mInitRect.right ? mInitRect.right : mInitRect.right + xMaxRightOffset;
                        yMaxBottom = mInitRect.bottom + yMaxBottomOffset >mInitRect.bottom ? mInitRect.bottom : mInitRect.bottom + yMaxBottomOffset;
                        if (mActionMovePoint.x > xMaxRight) {
                            mRect.right = xMaxRight;
                            xOffset = actionDownRectRight - mRect.right;
                        }
                        if (mActionMovePoint.y > yMaxBottom) {
                            mRect.bottom = yMaxBottom;
                            yOffset = actionDownRectBottom - mRect.bottom;
                        }
                        mPressPointIndex = 7;
                        break;
                }
                invalidate();
                return mPressPointIndex != -1;
            case MotionEvent.ACTION_UP:
                if (mPressPointIndex != -1) {
                    float xScale = mInitRect.width() / mRect.width();
                    float yScale = mInitRect.height() / mRect.height();
                    float scale = xScale > yScale ? yScale : xScale;
                    if (mCropListener!=null) {
                        mCropListener.onUp(mPressPointIndex, scale, xOffset, yOffset, mRect);
                    }
                    doAnimation(scale);
                }
                mPressPointIndex = -1;
                break;
        }
        return true;
    }

    private void doAnimation(float scale) {
        float left = (mInitRect.width() - mRect.width() * scale) / 2+rectOffset;
        float right = left + mRect.width() * scale;
        float top = (mInitRect.height() - mRect.height() * scale) / 2+rectOffset;
        float bottom = top + mRect.height() * scale;

        ValueAnimator rectAnimator = ValueAnimator.ofObject(new RectEvaluator(), mRect, new RectF(left, top, right, bottom));
        rectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRect = (RectF) animation.getAnimatedValue();
                invalidate();
            }
        });
        rectAnimator.setDuration(500);
        rectAnimator.start();
    }

    /**
     * 获取触点的位置
     * 0 1 2
     * 3   4
     * 5 6 7
     */
    private int getPosition(float x, float y) {
        if (mPressPointIndex > -1 && mPressPointIndex < 8) {
            return mPressPointIndex;
        }

        int POINT_RADIUS = 2500;//触点可响应区域的半径平方
        if ((x - mRect.left) * (x - mRect.left) + (y - mRect.top) * (y - mRect.top) < POINT_RADIUS) {
            return 0;
        } else if ((x - (mRect.left + mRect.right) / 2) * (x - (mRect.left + mRect.right) / 2) +
                (y - mRect.top) * (y - mRect.top) < POINT_RADIUS) {
            return 1;
        } else if ((x - mRect.right) * (x - mRect.right) + (y - mRect.top) * (y - mRect.top) < POINT_RADIUS) {
            return 2;
        } else if ((x - mRect.left) * (x - mRect.left) + (y - (mRect.top + mRect.bottom) / 2) *
                (y - (mRect.top + mRect.bottom) / 2) < POINT_RADIUS) {
            return 3;
        } else if ((x - mRect.right) * (x - mRect.right) + (y - (mRect.top + mRect.bottom) / 2) *
                (y - (mRect.top + mRect.bottom) / 2) < POINT_RADIUS) {
            return 4;
        } else if ((x - mRect.left) * (x - mRect.left) + (y - mRect.bottom) * (y - mRect.bottom) < POINT_RADIUS) {
            return 5;
        } else if ((x - (mRect.left + mRect.right) / 2) * (x - (mRect.left + mRect.right) / 2) +
                (y - mRect.bottom) * (y - mRect.bottom) < POINT_RADIUS) {
            return 6;
        } else if ((x - mRect.right) * (x - mRect.right) + (y - mRect.bottom) * (y - mRect.bottom) < POINT_RADIUS) {
            return 7;
        }
        return -1;
    }

    interface CropListener {
        void onUp(int positionIndex, float scale, float xOffset, float yOffset, RectF rectF);
    }

    /**
     * 裁剪框动画估值器
     */
    class RectEvaluator implements TypeEvaluator<RectF> {
        @Override
        public RectF evaluate(float fraction, RectF startValue, RectF endValue) {
            RectF rectf = new RectF();
            rectf.left = startValue.left + (endValue.left - startValue.left) * fraction;
            rectf.right = startValue.right + (endValue.right - startValue.right) * fraction;
            rectf.top = startValue.top + (endValue.top - startValue.top) * fraction;
            rectf.bottom = startValue.bottom + (endValue.bottom - startValue.bottom) * fraction;
            return rectf;
        }
    }
}
