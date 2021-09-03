package com.pivot.myandroiddemo.activity;

import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseActivity;

public class TextInputDemoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_input_demo);
        setToolbarTitle("TextInputDemo");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));
        
        TextInputLayout tilName = findViewById(R.id.til_name);
        TextInputLayout tilPwd = findViewById(R.id.til_pwd);
        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            String userName = tilName.getEditText().getText().toString();
            String userPwd = tilPwd.getEditText().getText().toString();
            if (!userName.equals("fjm") || !userPwd.equals("123")) {
                tilName.setError("用户信息有误");
                tilPwd.setError("用户信息有误");
            }
        });
    }
}
