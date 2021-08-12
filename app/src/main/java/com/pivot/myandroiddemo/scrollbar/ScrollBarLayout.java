package com.pivot.myandroiddemo.scrollbar;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.pivot.myandroiddemo.R;

/**
 * 所有页面的滚动条
 *
 * create by fanjiaming 2020/12/16
 */
public class ScrollBarLayout extends LinearLayout {
    public static final int OFFSET_NUM = 0;//视为超过一屏的偏移量
    public static final int DROP_DOWN_FRESH = 200;//下拉200视为下拉刷新操作
    private static final int SIDE_OFFSET = 30;
    private Context mContext;
    private ScrollBar mScrollBar;
    private int mBackGroundHeight;
    private int mDragHandleWidth;
    private int mDragHandleHeight;
    private OnBarListener mOnBarListener;
    private int mVisibleItemCount;
    private boolean mIsShow = false;
    private int mScrollState;// 0 列表停止滚动
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mHideTask = () -> {
        hideAnim();
        setVisibility(GONE);
    };

    public void hide() {
        if (!mIsShow || mHandler == null || mHideTask == null || getVisibility() == View.GONE) {
            return;
        }
        mIsShow = false;
        mHandler.removeCallbacks(mHideTask);
        mHandler.postDelayed(mHideTask, 1000);
    }

    public void removeHideTask() {
        if (mHandler == null || mHideTask == null) {
            return;
        }
        mIsShow = true;
        mHandler.removeCallbacks(mHideTask);
    }

    public ScrollBarLayout(Context context) {
        super(context);
        init(context);
    }

    public ScrollBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mBackGroundHeight = b - t;
    }

    public void setOnBarListener(OnBarListener onBarListener) {
        mOnBarListener = onBarListener;
    }

    private void init(Context context) {
        mContext = context;
        mScrollState = -1;
        mScrollBar = new ScrollBar(context);
        mDragHandleWidth = getResources().getDimensionPixelSize(R.dimen.scroll_bar_width);
        mDragHandleHeight = getResources().getDimensionPixelSize(R.dimen.scroll_bar_height);
        LayoutParams lp = new LayoutParams(mDragHandleWidth, mDragHandleHeight);
        lp.gravity = Gravity.TOP;
        mScrollBar.setLayoutParams(lp);
        addView(mScrollBar);
        mScrollBar.setOnTouchListener(new OnTouchListener() {
            float moveY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        removeHideTask();
                        moveY = event.getY();
                        // 隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        removeHideTask();
                        float y = mScrollBar.getY() + (event.getY() - moveY);
                        if (y >= 0 && y <= (mBackGroundHeight - mDragHandleHeight + SIDE_OFFSET * 2)) {
                            if (y < mDragHandleHeight * 0.15) {//直接显示第一个
                                y = 0;
                            } else if (y > mBackGroundHeight - mDragHandleHeight * 1.15 + SIDE_OFFSET * 2) {//直接显示最后一个
                                y = mBackGroundHeight - mDragHandleHeight + SIDE_OFFSET * 2;
                            }
                            mScrollBar.setTranslationY(y);
                            double progress = y / (mBackGroundHeight - mDragHandleHeight + SIDE_OFFSET * 2);
                            mOnBarListener.onBarProgressChanged(progress);
                            mOnBarListener.onBarControl(true);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (mScrollState == 0) {
                            hide();
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void scrollStateChanged(int scrollState, boolean isFullScreen) {
        if (isFullScreen) {
            if (scrollState == 1 && getVisibility() == View.GONE) {
                showAnim();
            }
            setVisibility(View.VISIBLE);
        } else {
            setVisibility(View.GONE);
            return;
        }
        mIsShow = true;
        mScrollState = scrollState;
        if (scrollState == 0) {
            hide();
        } else {
            removeHideTask();
        }
    }

    /**
     * 列表滑动时改变滚动条的位置
     * @param rowCount 每一行的item个数，ListView是1，GridView按传入的值
     */
    public void setScrollBarProgress(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int rowCount) {
        mVisibleItemCount = mVisibleItemCount == 0 ? (visibleItemCount + 1) : Math.max(mVisibleItemCount, visibleItemCount);
        if ((totalItemCount - mVisibleItemCount) > OFFSET_NUM) {
            int visibleCount = mVisibleItemCount / rowCount - 1;
            View c = view.getChildAt(0);
            int top = c.getTop();
            double progress = (firstVisibleItem / (rowCount * 1f) * c.getHeight() - top) / ((double) (totalItemCount / (rowCount * 1f) - visibleCount) * c.getHeight());
            float y = (float) ((mBackGroundHeight - mDragHandleHeight + SIDE_OFFSET * 2) * progress);
            mScrollBar.setTranslationY(Math.min(Math.max(0, y), mBackGroundHeight - mDragHandleHeight + SIDE_OFFSET * 2));
        } else {//列表未超过一屏
            setVisibility(View.GONE);
            mIsShow = false;
        }
    }
    public static boolean isRightToLeft(Context c) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                c.getResources().getConfiguration().getLayoutDirection() == LayoutDirection.RTL;
    }

    private void showAnim() {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, isRightToLeft(mContext) ? -1f : 1f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        anim.setDuration(150);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mScrollBar.setEnabled(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        startAnimation(anim);
    }

    private void hideAnim() {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_SELF, isRightToLeft(mContext) ? -1f : 1f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        anim.setDuration(150);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                mScrollBar.setEnabled(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        startAnimation(anim);
    }

    public interface OnBarListener {

        void onBarProgressChanged(double progress);

        void onBarControl(boolean underCtrl);

    }
}
