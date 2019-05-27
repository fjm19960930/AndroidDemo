package com.pivot.myandroiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.util.ConstUtil;
import com.pivot.myandroiddemo.util.UserDbUtils;
import com.zcolin.frame.util.ToastUtil;
import com.zcolin.gui.ZEditTextWithClear;
import com.zcolin.gui.ZEditTextWithPassword;

/**
 * 注册页面
 */
@ActivityParam(isShowToolBar = false)
public class RegisterActivity extends BaseActivity {
    private UserDbUtils dbUtils;
    private ZEditTextWithClear etUserName;
    private ZEditTextWithPassword etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbUtils = new UserDbUtils(this);

        etUserName = findViewById(R.id.et_register_name);
        etPassword = findViewById(R.id.et_register_pwd);
        Button btnSubmit = findViewById(R.id.btn_register_submit);
        Button btnReset = findViewById(R.id.btn_register_reset);
        btnSubmit.setOnClickListener(v -> {
            if (isCheck()) {
                if (dbUtils.isExist(etUserName.getText().toString().trim())) {
                    ToastUtil.toastLong("该用户名被占用！");
                } else {
                    long registerResult = dbUtils.register(etUserName.getText().toString().trim(), etPassword.getText().toString().trim());
                    if (registerResult > 0) {
                        ToastUtil.toastLong("注册成功！");
                        Intent intent = new Intent();
                        intent.putExtra("username", etUserName.getText().toString().trim());
                        intent.putExtra("password", etPassword.getText().toString().trim());
                        setResult(ConstUtil.RESULT_CODE_LOGIN_TO_REGISTER, intent);//将注册成功后的用户名和密码返回给LoginActivity
                        finish();
                    } else {
                        ToastUtil.toastLong("注册失败！");
                    }
                }
            } else {
                ToastUtil.toastLong("用户名或密码不可为空！");
            }
            
        });
        btnReset.setOnClickListener(v -> {
            etUserName.setText("");
            etPassword.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.dbUtils.getDb().close();
    }
    
    private boolean isCheck() {
        return !TextUtils.isEmpty(etUserName.getText().toString().trim()) && !TextUtils.isEmpty(etPassword.getText().toString().trim());
    }
}
