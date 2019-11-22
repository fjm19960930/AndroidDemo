package com.pivot.myandroiddemo.test.demoanimation.test3;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.pivot.myandroiddemo.R;

/**
 * Created by ASUS on 2019/9/24.
 */

public class LoadingImageView extends android.support.v7.widget.AppCompatImageView {
    private int mTop;
    private int mCurrentImgIndex = 0;//当前图片下标
    private int mImgCount = 4;//动画图片数量
    private ValueAnimator mAnimator;

    public LoadingImageView(Context context) {
        this(context, null);
    }

    public LoadingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTop = top;
    }

    private void init() {
        mAnimator = ValueAnimator.ofInt(0, 100, 0);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(2000);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                setTop(mTop - x);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setImageDrawable(getResources().getDrawable(R.mipmap.mine_icon_beauty, null));
            }

            @Override
            public void onAnimationEnd(Animator animation) {}

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {
                mCurrentImgIndex++;
                switch (mCurrentImgIndex % mImgCount) {
                    case 0:
                        setImageDrawable(getResources().getDrawable(R.mipmap.mine_icon_beauty, null));
                        break;
                    case 1:
                        setImageDrawable(getResources().getDrawable(R.mipmap.mine_icon_food, null));
                        break;
                    case 2:
                        setImageDrawable(getResources().getDrawable(R.mipmap.mine_icon_travel, null));
                        break;
                    case 3:
                        setImageDrawable(getResources().getDrawable(R.mipmap.mine_icon_msg, null));
                        break;
                }
            }
        });
    }
    
    public void start() {
        mAnimator.start();
    }
    
    public void cancel() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }
}
