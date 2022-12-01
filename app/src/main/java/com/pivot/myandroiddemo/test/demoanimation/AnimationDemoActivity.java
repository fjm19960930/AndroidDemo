package com.pivot.myandroiddemo.test.demoanimation;

import android.content.Intent;
import android.os.Bundle;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.test.demoanimation.test1.AnimationTest1Activity;
import com.pivot.myandroiddemo.test.demoanimation.test2.AnimationTest2Activity;
import com.pivot.myandroiddemo.test.demoanimation.test3.AnimationTest3Activity;
import com.pivot.myandroiddemo.test.demoanimation.test4.AnimationTest4Activity;

public class AnimationDemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_demo);
        setToolbarTitle("动画");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        
        getView(R.id.btn_anim_test1).setOnClickListener(v -> startActivity(new Intent(mActivity, AnimationTest1Activity.class)));
        getView(R.id.btn_anim_test2).setOnClickListener(v -> startActivity(new Intent(mActivity, AnimationTest2Activity.class)));
        getView(R.id.btn_anim_test3).setOnClickListener(v -> startActivity(new Intent(mActivity, AnimationTest3Activity.class)));
        getView(R.id.btn_anim_test4).setOnClickListener(v -> startActivity(new Intent(mActivity, AnimationTest4Activity.class)));
    }
}
