package com.pivot.myandroiddemo.test.demodraw.test1;

import android.os.Bundle;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;

@ActivityParam(isShowToolBar = false)
public class DrawTest1Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_test1);
    }
}
