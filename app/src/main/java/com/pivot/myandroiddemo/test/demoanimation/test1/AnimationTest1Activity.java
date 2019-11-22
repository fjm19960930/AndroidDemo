package com.pivot.myandroiddemo.test.demoanimation.test1;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;

/**
 * scale标签
 */

@ActivityParam(isShowToolBar = false)
public class AnimationTest1Activity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_test1);

        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.combination_anim);
        TextView tv = findViewById(R.id.anim_test1_tv);
        tv.startAnimation(animation);
    }
}
