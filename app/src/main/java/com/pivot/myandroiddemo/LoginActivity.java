package com.pivot.myandroiddemo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.util.ConstUtil;
import com.pivot.myandroiddemo.util.UserDbUtils;
import com.zcolin.frame.util.ActivityUtil;
import com.zcolin.frame.util.SPUtil;
import com.zcolin.frame.util.ToastUtil;
import com.zcolin.gui.ZEditTextWithClear;
import com.zcolin.gui.ZEditTextWithPassword;

/**
 * 登录界面
 */
@ActivityParam(isShowToolBar = false)
public class LoginActivity extends BaseActivity {
    private UserDbUtils dbUtils;
    private Cursor cursor;

    private ZEditTextWithClear etUserName;
    private ZEditTextWithPassword etPassword;
    private String userName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbUtils = new UserDbUtils(this);

        LinearLayout headLayout = findViewById(R.id.layout_head);
        headLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fly_from_top));
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);
        CheckBox cbRemPwd = findViewById(R.id.cb_rem_pwd);
        cbRemPwd.setChecked(true);//初始化为记住密码
        etUserName = findViewById(R.id.et_login_name);
        etPassword = findViewById(R.id.et_login_pwd);
        etUserName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fly_left_to_right));
        etPassword.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fly_right_to_left));
        etUserName.setText(SPUtil.getString("userName", ""));
        etPassword.setText(SPUtil.getString("password", ""));
        btnLogin.setOnClickListener(v -> {
            if (isCheck()) {
                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();
                cursor = dbUtils.queryPwd(userName);
                if (cursor != null && cursor.getCount() > 0) {
                    while(cursor.moveToNext()){
                        if (password.equals(cursor.getString(1))) {
                            if (cbRemPwd.isChecked()) {
                                SPUtil.putString("userName", userName);
                                SPUtil.putString("password", password);
                            } else {
                                SPUtil.putString("userName", "");
                                SPUtil.putString("password", "");
                            }
                            ActivityUtil.startActivity(LoginActivity.this, MainActivity.class, "username", userName);//开启MainActivity并传入用户名
                        } else {
                            ToastUtil.toastLong("密码错误！");
                        }
                    }
                } else {
                    ToastUtil.toastLong("该用户不存在！");
                }
            } else {
                ToastUtil.toastLong("用户名或密码不可为空!");
            }
        });
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityWithCallback(intent, (resultCode, data) -> {
                if (resultCode == ConstUtil.RESULT_CODE_LOGIN_TO_REGISTER) {
                    etUserName.setText(data.getStringExtra("username"));
                    etPassword.setText(data.getStringExtra("password"));
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.cursor.close();
        this.dbUtils.getDb().close();
    }

    private boolean isCheck() {
        return !TextUtils.isEmpty(etUserName.getText().toString().trim()) && !TextUtils.isEmpty(etPassword.getText().toString().trim());
    }
}
