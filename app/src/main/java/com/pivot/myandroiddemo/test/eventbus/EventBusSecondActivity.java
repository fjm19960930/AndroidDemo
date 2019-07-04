package com.pivot.myandroiddemo.test.eventbus;

import android.os.Bundle;
import android.view.View;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

@ActivityParam(isShowToolBar = true, isShowReturn = true)
public class EventBusSecondActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_second);
        setToolbarTitle("EventBus2");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        
        findViewById(R.id.btn_goto_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MyEvent("EventBus使用示例"));
                finish();
            }
        });
    }
}
