package com.pivot.myandroiddemo.test.demoanimation.test2;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;

@ActivityParam(isShowToolBar = false)
public class AnimationTest2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test2);

        /**
         * 有关动画的变化速率的问题是由Interpolator类来决定的，即插值器，它是一个接口，只要实现这个接口就可以自定义动画的速率
         * 引用插值器的方式：
         * 1.在XML动画文件的动画标签下引用插值器
         *      android:interpolator="@android:anim/accelerate_decelerate_interpolator"
         * 2.代码中通过setInterpolator方法来设置插值器
         */
        ImageView iv1 = findViewById(R.id.iv_anim_test2);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.3f, 1f, 1.3f, 
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//后四个参数可以保证缩放动画是从控件中心开始的
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setInterpolator(new BounceInterpolator());
//        scaleAnimation.setInterpolator(new EaseCubicInterpolator(0, 0.3f, 0.45f, 0.88f));
        scaleAnimation.setDuration(800);
        iv1.startAnimation(scaleAnimation);
        
        ImageView iv2 = findViewById(R.id.iv_anim_test2_loading);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        iv2.startAnimation(rotateAnimation);
        
        ImageView circle1 = findViewById(R.id.iv_anim_test2_circle1);
        ImageView circle2 = findViewById(R.id.iv_anim_test2_circle2);
        ImageView circle3 = findViewById(R.id.iv_anim_test2_circle3);
        ImageView circle4 = findViewById(R.id.iv_anim_test2_circle4);
        Animation animation1 = AnimationUtils.loadAnimation(mActivity, R.anim.scan_alpha_anim);
        Animation animation2 = AnimationUtils.loadAnimation(mActivity, R.anim.scan_alpha_anim);animation2.setStartOffset(600);
        Animation animation3 = AnimationUtils.loadAnimation(mActivity, R.anim.scan_alpha_anim);animation3.setStartOffset(1200);
        Animation animation4 = AnimationUtils.loadAnimation(mActivity, R.anim.scan_alpha_anim);animation4.setStartOffset(1800);
        findViewById(R.id.tv_anim_test2).setOnClickListener(v -> {
            circle1.startAnimation(animation1);
            circle2.startAnimation(animation2);
            circle3.startAnimation(animation3);
            circle4.startAnimation(animation4);
        });
    }
}
