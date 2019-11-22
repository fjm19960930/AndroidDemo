package com.pivot.myandroiddemo.test.demoview;

import android.content.Intent;
import android.os.Bundle;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.test.demoview.test1.ViewTest1Activity;

public class ViewDemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demo);
        setToolbarTitle("视图");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));

        getView(R.id.btn_view_test1).setOnClickListener(v -> startActivity(new Intent(mActivity, ViewTest1Activity.class)));
    }
}
