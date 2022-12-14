package com.pivot.myandroiddemo.test.demodraw;

import android.content.Intent;
import android.os.Bundle;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.test.demodraw.telescope.DrawTest2Activity;
import com.pivot.myandroiddemo.test.demodraw.test1.DrawTest1Activity;

public class DrawDemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_demo);
        setToolbarTitle("绘图");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        
        getView(R.id.btn_draw_test1).setOnClickListener(v -> startActivity(new Intent(mActivity, DrawTest1Activity.class)));
        getView(R.id.btn_draw_test2).setOnClickListener(v -> startActivity(new Intent(mActivity, DrawTest2Activity.class)));
    }
}
