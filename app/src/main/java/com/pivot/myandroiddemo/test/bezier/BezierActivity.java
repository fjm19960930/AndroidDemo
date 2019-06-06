package com.pivot.myandroiddemo.test.bezier;

import android.os.Bundle;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;

/**
 * 贝塞尔曲线Demo
 */
@ActivityParam(isShowReturn = true, isShowToolBar = true)
public class BezierActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        setToolbarTitle("BezierLineDemo");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        
        BezierView1 bezierView1 = findViewById(R.id.bv1);
        BezierView2 bezierView2 = findViewById(R.id.bv2);
        findViewById(R.id.btn_bezier_reset).setOnClickListener(v -> bezierView1.reset());
        bezierView2.startAnim();
    }
}
